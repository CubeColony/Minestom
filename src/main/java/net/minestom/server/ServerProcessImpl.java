package net.minestom.server;

import net.minestom.server.acquirable.Acquirable;
import net.minestom.server.advancements.AdvancementManager;
import net.minestom.server.adventure.bossbar.BossBarManager;
import net.minestom.server.command.CommandManager;
import net.minestom.server.data.DataManager;
import net.minestom.server.exception.ExceptionManager;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;
import net.minestom.server.gamedata.tags.TagManager;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.network.PacketProcessor;
import net.minestom.server.network.socket.Server;
import net.minestom.server.network.socket.Worker;
import net.minestom.server.recipe.RecipeManager;
import net.minestom.server.scoreboard.TeamManager;
import net.minestom.server.storage.StorageLocation;
import net.minestom.server.storage.StorageManager;
import net.minestom.server.terminal.MinestomTerminal;
import net.minestom.server.thread.MinestomThreadPool;
import net.minestom.server.thread.ThreadDispatcher;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.PacketUtils;
import net.minestom.server.utils.validate.Check;
import net.minestom.server.world.DimensionTypeManager;
import net.minestom.server.world.biomes.BiomeManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.function.Consumer;

final class ServerProcessImpl implements ServerProcess {
    public final static Logger LOGGER = LoggerFactory.getLogger(ServerProcessImpl.class);

    private final ExceptionManager exception;
    private final ExtensionManager extension;
    private final ConnectionManager connection;
    private final PacketProcessor packetProcessor;
    private final PacketListenerManager packetListener;
    private final InstanceManager instance;
    private final BlockManager block;
    private final CommandManager command;
    private final RecipeManager recipe;
    private final StorageManager storage;
    private final DataManager data;
    private final TeamManager team;
    private final SchedulerManager scheduler;
    private final BenchmarkManager benchmark;
    private final DimensionTypeManager dimension;
    private final BiomeManager biome;
    private final AdvancementManager advancement;
    private final BossBarManager bossBar;
    private final UpdateManager update;
    private final TagManager tag;
    private final Server server;

    private final ThreadDispatcher dispatcher;
    private final Ticker ticker;

    private boolean terminalEnabled = System.getProperty("minestom.terminal.disabled") == null;
    private boolean started;
    private volatile boolean stopping;

    public ServerProcessImpl() throws IOException {
        this.exception = new ExceptionManager();
        this.extension = new ExtensionManager();
        this.connection = new ConnectionManager(this);
        this.packetProcessor = new PacketProcessor();
        this.packetListener = new PacketListenerManager(this);
        this.instance = new InstanceManager();
        this.block = new BlockManager();
        this.command = new CommandManager();
        this.recipe = new RecipeManager();
        this.storage = new StorageManager();
        this.data = new DataManager();
        this.team = new TeamManager(this);
        this.scheduler = new SchedulerManager();
        this.benchmark = new BenchmarkManager();
        this.dimension = new DimensionTypeManager();
        this.biome = new BiomeManager();
        this.advancement = new AdvancementManager();
        this.bossBar = new BossBarManager();
        this.update = new UpdateManager();
        this.tag = new TagManager();
        this.server = new Server(packetProcessor);

        this.dispatcher = ThreadDispatcher.singleThread();
        this.ticker = new TickerImpl();
    }

    @Override
    public @NotNull ConnectionManager connection() {
        return connection;
    }

    @Override
    public @NotNull InstanceManager instance() {
        return instance;
    }

    @Override
    public @NotNull BlockManager block() {
        return block;
    }

    @Override
    public @NotNull CommandManager command() {
        return command;
    }

    @Override
    public @NotNull RecipeManager recipe() {
        return recipe;
    }

    @Override
    public @NotNull StorageManager storage() {
        return storage;
    }

    @Override
    public @NotNull DataManager data() {
        return data;
    }

    @Override
    public @NotNull TeamManager team() {
        return team;
    }

    @Override
    public @NotNull SchedulerManager scheduler() {
        return scheduler;
    }

    @Override
    public @NotNull BenchmarkManager benchmark() {
        return benchmark;
    }

    @Override
    public @NotNull DimensionTypeManager dimension() {
        return dimension;
    }

    @Override
    public @NotNull BiomeManager biome() {
        return biome;
    }

    @Override
    public @NotNull AdvancementManager advancement() {
        return advancement;
    }

    @Override
    public @NotNull BossBarManager bossBar() {
        return bossBar;
    }

    @Override
    public @NotNull ExtensionManager extension() {
        return extension;
    }

    @Override
    public @NotNull UpdateManager update() {
        return update;
    }

    @Override
    public @NotNull TagManager tag() {
        return tag;
    }

    @Override
    public @NotNull ExceptionManager exception() {
        return exception;
    }

    @Override
    public @NotNull PacketListenerManager packetListener() {
        return packetListener;
    }

    @Override
    public @NotNull PacketProcessor packetProcessor() {
        return packetProcessor;
    }

    @Override
    public @NotNull Server server() {
        return server;
    }

    @Override
    public @NotNull ThreadDispatcher dispatcher() {
        return dispatcher;
    }

    @Override
    public @NotNull Ticker ticker() {
        return ticker;
    }

    @Override
    public void start(@NotNull SocketAddress socketAddress) {
        Check.stateCondition(started, "The server is already started");
        this.started = true;
        LOGGER.info("Starting Minestom server.");
        //update.start();
        // Init server
        try {
            server.init(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (extension.shouldLoadOnStartup()) {
            final long loadStartTime = System.nanoTime();
            // Load extensions
            extension.loadExtensions();
            // Init extensions
            extension.getExtensions().forEach(Extension::preInitialize);
            extension.getExtensions().forEach(Extension::initialize);
            extension.getExtensions().forEach(Extension::postInitialize);
            final double loadTime = MathUtils.round((System.nanoTime() - loadStartTime) / 1_000_000D, 2);
            LOGGER.info("Extensions loaded in {}ms", loadTime);
        } else {
            LOGGER.warn("Extension loadOnStartup option is set to false, extensions are therefore neither loaded or initialized.");
        }

        // Start server
        server.start();

        LOGGER.info("Minestom server started successfully.");

        if (terminalEnabled) {
            MinestomTerminal.start();
        }

        // Stop the server on SIGINT
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    @Override
    public void stop() {
        if (stopping) return;
        stopping = true;
        LOGGER.info("Stopping Minestom server.");
        extension.unloadAllExtensions();
        update.stop();
        scheduler.shutdown();
        connection.shutdown();
        server.stop();
        storage.getLoadedLocations().forEach(StorageLocation::close);
        LOGGER.info("Unloading all extensions.");
        extension.shutdown();
        LOGGER.info("Shutting down all thread pools.");
        benchmark.disable();
        MinestomTerminal.stop();
        MinestomThreadPool.shutdownAll();
        LOGGER.info("Minestom server stopped successfully.");
    }

    @Override
    public boolean isAlive() {
        return started && !stopping;
    }

    private final class TickerImpl implements Ticker {
        @Override
        public void tick(long time) {
            long currentTime = System.nanoTime();
            final long tickStart = System.currentTimeMillis();

            scheduler().processTick();

            // Waiting players update (newly connected clients waiting to get into the server)
            connection().updateWaitingPlayers();

            // Keep Alive Handling
            connection().handleKeepAlive(tickStart);

            // Server tick (chunks/entities)
            serverTick(tickStart);

            // Flush all waiting packets
            PacketUtils.flush();
            server().workers().forEach(Worker::flush);

            // the time that the tick took in nanoseconds
            final long tickTime = System.nanoTime() - currentTime;

            // Monitoring FIXME
            var tickMonitors = new ArrayList<Consumer<TickMonitor>>();
            if (!tickMonitors.isEmpty()) {
                final double acquisitionTimeMs = Acquirable.getAcquiringTime() / 1e6D;
                final double tickTimeMs = tickTime / 1e6D;
                final TickMonitor tickMonitor = new TickMonitor(tickTimeMs, acquisitionTimeMs);
                for (Consumer<TickMonitor> consumer : tickMonitors) {
                    consumer.accept(tickMonitor);
                }
                Acquirable.resetAcquiringTime();
            }
        }

        private void serverTick(long tickStart) {
            // Tick all instances
            for (Instance instance : instance().getInstances()) {
                try {
                    instance.tick(tickStart);
                } catch (Exception e) {
                    MinecraftServer.getExceptionManager().handleException(e);
                }
            }
            // Tick all chunks (and entities inside)
            dispatcher().updateAndAwait(tickStart);

            // Clear removed entities & update threads
            final long tickTime = System.currentTimeMillis() - tickStart;
            dispatcher().refreshThreads(tickTime);
        }
    }
}
