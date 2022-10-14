package net.minestom.server.cubecolony.redis;

import com.cubecolony.api.annotation.Documented;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author LBuke (Teddeh)
 */
@Documented
public interface RedisMessageListener<T extends Redisable> {

    /**
     * @param from    - Original sender of the Redisable message.
     * @param message - Redisable message
     */
    void onReceive(@NotNull UUID from, T message);
}
