package net.minestom.server.cubecolony.redis;

import com.cubecolony.api.annotation.AwaitingDocumentation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LBuke (Teddeh)
 */
@AwaitingDocumentation
public final class MessageReader extends JedisPubSub {

    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReader.class);
    private final UUID uuid;

    MessageReader(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onMessage(String channel, String message) {
        try {
            final JsonObject label;
            try {
                label = (JsonObject) JsonParser.parseString(message);
            } catch (JsonSyntaxException e) {
                return;
            }

            final String clazz = label.get("class").getAsString();
            final UUID sender = UUID.fromString(label.get("sender").getAsString());

            UUID recipient = null;
            if (label.has("recipient")) {
                recipient = UUID.fromString(label.get("recipient").getAsString());
            }

            if (Objects.nonNull(recipient) && !this.uuid.equals(recipient)) {
                return;
            }

            if (this.uuid.equals(sender)) {
                return;
            }

            try {
                final Class<? extends Redisable> rediableClass = (Class<? extends Redisable>) Class.forName(clazz);
                final Redisable msg = GSON.fromJson(label.getAsJsonObject("content"), rediableClass);
                final List<RedisMessageListener> listenerList = BaseRedisDatabase.getListeners().get(rediableClass);
                if (listenerList != null) {
                    listenerList.forEach(c -> c.onReceive(sender, msg));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPMessage(String s, String s1, String s2) {

    }

    @Override
    public void onSubscribe(String s, int i) {

    }

    @Override
    public void onUnsubscribe(String s, int i) {

    }

    @Override
    public void onPUnsubscribe(String s, int i) {

    }

    @Override
    public void onPSubscribe(String s, int i) {

    }
}
