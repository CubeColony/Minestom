package net.minestom.server.cubecolony.redis;

import com.cubecolony.api.annotation.AwaitingDocumentation;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;
import java.util.UUID;

/**
 * @author LBuke (Teddeh)
 */
@AwaitingDocumentation
public record MessageWriter(UUID sender, JedisPool pool) {

    public MessageWriter(@NotNull UUID sender, @NotNull JedisPool pool) {
        this.sender = sender;
        this.pool = pool;
    }

    void publishPacket(Redisable message, String channel, @Nullable UUID recipient) {
        final JsonObject label = new JsonObject();
        label.addProperty("class", message.getClass().getName());

        label.add("sender", JsonParser.parseString(this.sender.toString()));

        if (Objects.nonNull(recipient)) {
            label.add("recipient", JsonParser.parseString(recipient.toString()));
        }

        label.add("content", JsonParser.parseString(message.toJson()));

        try (final Jedis jedis = this.pool.getResource()) {
            jedis.publish(channel, label.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
