package net.minestom.server.cubecolony.redis;

import com.cubecolony.api.annotation.AwaitingDocumentation;

/**
 * @author LBuke (Teddeh)
 */
@AwaitingDocumentation
public interface RedisCache<T> {
    void updated(T object);
}
