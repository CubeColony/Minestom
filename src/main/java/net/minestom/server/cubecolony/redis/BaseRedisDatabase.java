package net.minestom.server.cubecolony.redis;

import com.cubecolony.api.util.ByteUtil;
import net.minestom.server.cubecolony.redis.message.RedisCacheUpdated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author LBuke (Teddeh)
 */
@SuppressWarnings({"rawtypes"})
public final class BaseRedisDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRedisDatabase.class);

    private static final HashMap<Class<? extends Redisable>, List<RedisMessageListener>> LISTENERS = new HashMap<>();

    private static BaseRedisDatabase instance;

    private JedisPool jedisPool;
    private MessageReader reader;
    private MessageWriter writer;

    private Thread thread;

    private UUID uuid;
    private String[] channels;

    public void init(UUID uuid, String... channels) {
        this.uuid = uuid;
        this.channels = channels;

        this.connect();
    }

    public void connect() {
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(256);
        config.setMaxIdle(32);
        config.setMaxIdle(8);
        config.setBlockWhenExhausted(true);
        config.setMaxWait(Duration.ofSeconds(5));
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTime(Duration.ofSeconds(30));
        config.setTimeBetweenEvictionRuns(Duration.ofSeconds(15));
        config.setNumTestsPerEvictionRun(-1);

        // TODO: makes it configurable
        this.jedisPool = new JedisPool(
                config,
                "127.0.0.1",
                6379,
                1000 * 30
        );

        if (this.jedisPool.isClosed()) {
            LOGGER.info("redis pool is closed.");
        }

        try (final Jedis jedis = this.jedisPool.getResource()) {
            LOGGER.info("Connected to redis, ping: " + jedis.ping());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.reader = new MessageReader(this.uuid);
        this.writer = new MessageWriter(this.uuid, this.jedisPool);

        this.thread = new Thread("Redis Subscriber") {
            @Override
            public void run() {
                try (final Jedis jedis = BaseRedisDatabase.this.getJedisPool().getResource()) {
                    jedis.subscribe(BaseRedisDatabase.this.reader, BaseRedisDatabase.this.channels);
                } catch (JedisConnectionException e) {
                    e.printStackTrace();
                    BaseRedisDatabase.this.reconnect();
                }
            }
        };
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public void close() {
        if (this.reader.isSubscribed()) {
            this.reader.unsubscribe();
        }

        if (this.jedisPool != null) {
            this.jedisPool.destroy();
        }

        this.thread.interrupt();
        this.thread = null;
    }

    public void reconnect() {
        LOGGER.info("ATTEMPTING REDIS RECONNECTION");
        this.close();
        this.connect();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    /**
     * Send a serialized message to the redis database.
     *
     * @param message   - Redis message instance
     * @param channel   - Channel which should receive this message
     * @param recipient - Server target recipient
     */
    public static void message(Redisable message, String channel, UUID recipient) {
        getInstance().writer.publishPacket(message, channel, recipient);
    }

    /**
     * Send a serialized message to the redis database.
     *
     * @param message - Redis message instance
     * @param channel - Channel which should receive this message
     */
    public static void message(Redisable message, String channel) {
        getInstance().writer.publishPacket(message, channel, null);
    }

    public static <T> @Nullable T get(@NotNull String key) {
        try (final Jedis jedis = getInstance().jedisPool.getResource()) {
            final byte[] bytes = key.getBytes();
            return ByteUtil.fromByteArray(jedis.get(bytes));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static <T> void set(@NotNull String key, @NotNull T object, int seconds) {
        try (final Jedis jedis = getInstance().jedisPool.getResource()) {
            final byte[] keyBytes = key.getBytes();
            final byte[] objectBytes = ByteUtil.toByteArray(object);
            jedis.set(keyBytes, objectBytes);
            jedis.expire(keyBytes, seconds);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        final RedisCacheUpdated cacheUpdated = new RedisCacheUpdated(key, seconds);
        BaseRedisDatabase.message(cacheUpdated, "global");
    }

    /**
     * Register a redis listener.
     *
     * @param clazz    - Redis message class
     * @param listener - Redis listener
     * @param <T>      - Redis message type for the listener
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Redisable> void register(Class<T> clazz, RedisMessageListener<T> listener) {
        if (LISTENERS.containsKey(clazz)) {
            LISTENERS.get(clazz).add(listener);
        } else {
            final List<RedisMessageListener> list = new ArrayList<>();
            list.add(listener);
            LISTENERS.put(clazz, list);
        }
    }

    /**
     * Get a Mapped list of all registered redis listeners.
     *
     * @return Mapped registered listeners
     */
    static HashMap<Class<? extends Redisable>, List<RedisMessageListener>> getListeners() {
        return LISTENERS;
    }

    public static @NotNull BaseRedisDatabase getInstance() {
        if (instance == null) {
            instance = new BaseRedisDatabase();
        }

        return instance;
    }

    public static @NotNull BaseRedisDatabase get() {
        return getInstance();
    }
}
