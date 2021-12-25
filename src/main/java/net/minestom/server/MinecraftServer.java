package net.minestom.server;

import net.minestom.server.advancements.AdvancementManager;
import net.minestom.server.adventure.bossbar.BossBarManager;
import net.minestom.server.command.CommandManager;
import net.minestom.server.data.DataManager;
import net.minestom.server.data.DataType;
import net.minestom.server.data.SerializableData;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.exception.ExceptionManager;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;
import net.minestom.server.gamedata.tags.TagManager;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.network.PacketProcessor;
import net.minestom.server.network.packet.server.play.PluginMessagePacket;
import net.minestom.server.network.packet.server.play.ServerDifficultyPacket;
import net.minestom.server.network.socket.Server;
import net.minestom.server.ping.ResponseDataConsumer;
import net.minestom.server.recipe.RecipeManager;
import net.minestom.server.scoreboard.TeamManager;
import net.minestom.server.storage.StorageManager;
import net.minestom.server.thread.TickSchedulerThread;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.PacketUtils;
import net.minestom.server.utils.validate.Check;
import net.minestom.server.world.Difficulty;
import net.minestom.server.world.DimensionTypeManager;
import net.minestom.server.world.biomes.BiomeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The main server class used to start the server and retrieve all the managers.
 * <p>
 * The server needs to be initialized with {@link #init()} and started with {@link #start(String, int)}.
 * You should register all of your dimensions, biomes, commands, events, etc... in-between.
 */
public final class MinecraftServer {

    public final static Logger LOGGER = LoggerFactory.getLogger(MinecraftServer.class);

    public static final String VERSION_NAME = "1.18.1";
    public static final int PROTOCOL_VERSION = 757;

    // Threads
    public static final String THREAD_NAME_BENCHMARK = "Ms-Benchmark";

    public static final String THREAD_NAME_TICK_SCHEDULER = "Ms-TickScheduler";
    public static final String THREAD_NAME_TICK = "Ms-Tick";

    public static final String THREAD_NAME_BLOCK_BATCH = "Ms-BlockBatchPool";
    public static final int THREAD_COUNT_BLOCK_BATCH = getThreadCount("minestom.block-thread-count",
            Runtime.getRuntime().availableProcessors() / 2);

    // Config
    // Can be modified at performance cost when increased
    public static final int TICK_PER_SECOND = Integer.getInteger("minestom.tps", 20);
    public static final int TICK_MS = 1000 / TICK_PER_SECOND;

    // Network monitoring
    private static int rateLimit = 300;
    private static int maxPacketSize = 30_000;

    // In-Game Manager
    private static volatile ServerProcess serverProcess;

    private static final GlobalEventHandler GLOBAL_EVENT_HANDLER = new GlobalEventHandler();

    // Data
    private static boolean initialized;
    private static boolean started;
    private static volatile boolean stopping;

    private static int chunkViewDistance = Integer.getInteger("minestom.chunk-view-distance", 8);
    private static int entityViewDistance = Integer.getInteger("minestom.entity-view-distance", 5);
    private static int compressionThreshold = 256;
    private static boolean terminalEnabled = System.getProperty("minestom.terminal.disabled") == null;
    private static ResponseDataConsumer responseDataConsumer;
    private static String brandName = "Minestom";
    private static Difficulty difficulty = Difficulty.NORMAL;

    public static MinecraftServer init() {
        try {
            serverProcess = new ServerProcessImpl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initialized = true;
        return new MinecraftServer();
    }

    /**
     * Gets the current server brand name.
     *
     * @return the server brand name
     */
    @NotNull
    public static String getBrandName() {
        return brandName;
    }

    /**
     * Changes the server brand name and send the change to all connected players.
     *
     * @param brandName the server brand name
     * @throws NullPointerException if {@code brandName} is null
     */
    public static void setBrandName(@NotNull String brandName) {
        MinecraftServer.brandName = brandName;
        PacketUtils.broadcastPacket(PluginMessagePacket.getBrandPacket());
    }

    /**
     * Gets the maximum number of packets a client can send over 1 second.
     *
     * @return the packet count limit over 1 second, 0 if not enabled
     */
    public static int getRateLimit() {
        return rateLimit;
    }

    /**
     * Changes the number of packet a client can send over 1 second without being disconnected.
     *
     * @param rateLimit the number of packet, 0 to disable
     */
    public static void setRateLimit(int rateLimit) {
        MinecraftServer.rateLimit = rateLimit;
    }

    /**
     * Gets the maximum packet size (in bytes) that a client can send without getting disconnected.
     *
     * @return the maximum packet size
     */
    public static int getMaxPacketSize() {
        return maxPacketSize;
    }

    /**
     * Changes the maximum packet size (in bytes) that a client can send without getting disconnected.
     *
     * @param maxPacketSize the new max packet size
     */
    public static void setMaxPacketSize(int maxPacketSize) {
        MinecraftServer.maxPacketSize = maxPacketSize;
    }

    /**
     * Gets the server difficulty showed in game option.
     *
     * @return the server difficulty
     */
    @NotNull
    public static Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Changes the server difficulty and send the appropriate packet to all connected clients.
     *
     * @param difficulty the new server difficulty
     */
    public static void setDifficulty(@NotNull Difficulty difficulty) {
        MinecraftServer.difficulty = difficulty;
        PacketUtils.broadcastPacket(new ServerDifficultyPacket(difficulty, true));
    }

    public static @UnknownNullability ServerProcess getServerProcess() {
        return serverProcess;
    }

    /**
     * Gets the global event handler.
     * <p>
     * Used to register event callback at a global scale.
     *
     * @return the global event handler
     */
    public static @NotNull GlobalEventHandler getGlobalEventHandler() {
        return GLOBAL_EVENT_HANDLER;
    }

    /**
     * Gets the manager handling all incoming packets
     *
     * @return the packet listener manager
     */
    public static PacketListenerManager getPacketListenerManager() {
        return serverProcess.packetListener();
    }

    /**
     * Gets the manager handling all registered instances.
     *
     * @return the instance manager
     */
    public static InstanceManager getInstanceManager() {
        return serverProcess.instance();
    }

    /**
     * Gets the manager handling {@link net.minestom.server.instance.block.BlockHandler block handlers}
     * and {@link BlockPlacementRule placement rules}.
     *
     * @return the block manager
     */
    public static BlockManager getBlockManager() {
        return serverProcess.block();
    }

    /**
     * Gets the manager handling commands.
     *
     * @return the command manager
     */
    public static CommandManager getCommandManager() {
        return serverProcess.command();
    }

    /**
     * Gets the manager handling recipes show to the clients.
     *
     * @return the recipe manager
     */
    public static RecipeManager getRecipeManager() {
        return serverProcess.recipe();
    }

    /**
     * Gets the manager handling storage.
     *
     * @return the storage manager
     */
    @Deprecated
    public static StorageManager getStorageManager() {
        return serverProcess.storage();
    }

    /**
     * Gets the manager handling {@link DataType} used by {@link SerializableData}.
     *
     * @return the data manager
     */
    @Deprecated
    public static DataManager getDataManager() {
        return serverProcess.data();
    }

    /**
     * Gets the manager handling teams.
     *
     * @return the team manager
     */
    public static TeamManager getTeamManager() {
        return serverProcess.team();
    }

    /**
     * Gets the manager handling scheduled tasks.
     *
     * @return the scheduler manager
     */
    public static SchedulerManager getSchedulerManager() {
        return serverProcess.scheduler();
    }

    /**
     * Gets the manager handling server monitoring.
     *
     * @return the benchmark manager
     */
    public static BenchmarkManager getBenchmarkManager() {
        return serverProcess.benchmark();
    }

    /**
     * Gets the exception manager for exception handling.
     *
     * @return the exception manager
     */
    public static ExceptionManager getExceptionManager() {
        return serverProcess.exception();
    }

    /**
     * Gets the manager handling server connections.
     *
     * @return the connection manager
     */
    public static ConnectionManager getConnectionManager() {
        return serverProcess.connection();
    }

    /**
     * Gets the boss bar manager.
     *
     * @return the boss bar manager
     */
    public static BossBarManager getBossBarManager() {
        return serverProcess.bossBar();
    }

    /**
     * Gets the object handling the client packets processing.
     * <p>
     * Can be used if you want to convert a buffer to a client packet object.
     *
     * @return the packet processor
     */
    public static PacketProcessor getPacketProcessor() {
        return serverProcess.packetProcessor();
    }

    /**
     * Gets if the server is up and running.
     *
     * @return true if the server is started
     */
    public static boolean isStarted() {
        return started;
    }

    /**
     * Gets if the server is currently being shutdown using {@link #stopCleanly()}.
     *
     * @return true if the server is being stopped
     */
    public static boolean isStopping() {
        return stopping;
    }

    /**
     * Gets the chunk view distance of the server.
     *
     * @return the chunk view distance
     */
    public static int getChunkViewDistance() {
        return chunkViewDistance;
    }

    /**
     * Changes the chunk view distance of the server.
     *
     * @param chunkViewDistance the new chunk view distance
     * @throws IllegalArgumentException if {@code chunkViewDistance} is not between 2 and 32
     * @deprecated should instead be defined with a java property
     */
    @Deprecated
    public static void setChunkViewDistance(int chunkViewDistance) {
        Check.stateCondition(started, "You cannot change the chunk view distance after the server has been started.");
        Check.argCondition(!MathUtils.isBetween(chunkViewDistance, 2, 32),
                "The chunk view distance must be between 2 and 32");
        MinecraftServer.chunkViewDistance = chunkViewDistance;
    }

    /**
     * Gets the entity view distance of the server.
     *
     * @return the entity view distance
     */
    public static int getEntityViewDistance() {
        return entityViewDistance;
    }

    /**
     * Changes the entity view distance of the server.
     *
     * @param entityViewDistance the new entity view distance
     * @throws IllegalArgumentException if {@code entityViewDistance} is not between 0 and 32
     * @deprecated should instead be defined with a java property
     */
    @Deprecated
    public static void setEntityViewDistance(int entityViewDistance) {
        Check.stateCondition(started, "You cannot change the entity view distance after the server has been started.");
        Check.argCondition(!MathUtils.isBetween(entityViewDistance, 0, 32),
                "The entity view distance must be between 0 and 32");
        MinecraftServer.entityViewDistance = entityViewDistance;
    }

    /**
     * Gets the compression threshold of the server.
     *
     * @return the compression threshold, 0 means that compression is disabled
     */
    public static int getCompressionThreshold() {
        return compressionThreshold;
    }

    /**
     * Changes the compression threshold of the server.
     * <p>
     * WARNING: this need to be called before {@link #start(String, int, ResponseDataConsumer)}.
     *
     * @param compressionThreshold the new compression threshold, 0 to disable compression
     * @throws IllegalStateException if this is called after the server started
     */
    public static void setCompressionThreshold(int compressionThreshold) {
        Check.stateCondition(started, "The compression threshold cannot be changed after the server has been started.");
        MinecraftServer.compressionThreshold = compressionThreshold;
    }

    /**
     * Gets if the built in Minestom terminal is enabled.
     *
     * @return true if the terminal is enabled
     */
    public static boolean isTerminalEnabled() {
        return terminalEnabled;
    }

    /**
     * Enabled/disables the built in Minestom terminal.
     *
     * @param enabled true to enable, false to disable
     */
    public static void setTerminalEnabled(boolean enabled) {
        Check.stateCondition(started, "Terminal settings may not be changed after starting the server.");
        MinecraftServer.terminalEnabled = enabled;
    }

    /**
     * Gets the consumer executed to show server-list data.
     *
     * @return the response data consumer
     * @deprecated listen to the {@link net.minestom.server.event.server.ServerListPingEvent} instead
     */
    @Deprecated
    public static ResponseDataConsumer getResponseDataConsumer() {
        return responseDataConsumer;
    }

    /**
     * Gets the manager handling dimensions.
     *
     * @return the dimension manager
     */
    public static DimensionTypeManager getDimensionTypeManager() {
        return serverProcess.dimension();
    }

    /**
     * Gets the manager handling biomes.
     *
     * @return the biome manager
     */
    public static BiomeManager getBiomeManager() {
        return serverProcess.biome();
    }

    /**
     * Gets the manager handling advancements.
     *
     * @return the advancement manager
     */
    public static AdvancementManager getAdvancementManager() {
        return serverProcess.advancement();
    }

    /**
     * Get the manager handling {@link Extension}.
     *
     * @return the extension manager
     */
    public static ExtensionManager getExtensionManager() {
        return serverProcess.extension();
    }

    /**
     * Gets the manager handling tags.
     *
     * @return the tag manager
     */
    public static TagManager getTagManager() {
        return serverProcess.tag();
    }

    /**
     * Gets the manager handling the server ticks.
     *
     * @return the update manager
     */
    public static UpdateManager getUpdateManager() {
        return serverProcess.update();
    }

    public static Server getServer() {
        return serverProcess.server();
    }

    /**
     * Starts the server.
     * <p>
     * It should be called after {@link #init()} and probably your own initialization code.
     *
     * @param address              the server address
     * @param port                 the server port
     * @param responseDataConsumer the response data consumer, can be null
     * @throws IllegalStateException if called before {@link #init()} or if the server is already running
     * @deprecated use {@link #start(String, int)} and listen to the {@link net.minestom.server.event.server.ServerListPingEvent} event instead of ResponseDataConsumer
     */
    @Deprecated
    public void start(@NotNull String address, int port, @Nullable ResponseDataConsumer responseDataConsumer) {
        MinecraftServer.responseDataConsumer = responseDataConsumer;
        start(address, port);
    }

    /**
     * Starts the server.
     * <p>
     * It should be called after {@link #init()} and probably your own initialization code.
     *
     * @param address the server address
     * @param port    the server port
     * @throws IllegalStateException if called before {@link #init()} or if the server is already running
     */
    public void start(@NotNull String address, int port) {
        serverProcess.start(new InetSocketAddress(address, port));
        new TickSchedulerThread(serverProcess).start();
    }

    /**
     * Stops this server properly (saves if needed, kicking players, etc.)
     */
    public static void stopCleanly() {
        serverProcess.stop();
    }

    private static int getThreadCount(@NotNull String property, int count) {
        return Integer.getInteger(property, Math.max(1, count));
    }
}
