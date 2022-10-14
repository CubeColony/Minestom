package net.minestom.server.cubecolony.redis.message;

import net.minestom.server.cubecolony.redis.Redisable;

import java.io.Serializable;

/**
 * @author LBuke (Teddeh)
 */
@SuppressWarnings("unused")
public final class RedisCacheUpdated implements Redisable, Serializable {

    private final String key;
    private final byte[] keyBytes;
    private final int expireInSeconds;

    public RedisCacheUpdated(String key, int expireInSeconds) {
        this.key = key;
        this.keyBytes = key.getBytes();
        this.expireInSeconds = expireInSeconds;
    }

    public String getKey() {
        return this.key;
    }

    public byte[] getKeyBytes() {
        return this.keyBytes;
    }

    public int getExpireInSeconds() {
        return this.expireInSeconds;
    }
}
