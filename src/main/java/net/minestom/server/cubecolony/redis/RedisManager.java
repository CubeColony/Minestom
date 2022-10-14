package net.minestom.server.cubecolony.redis;


import com.cubecolony.api.annotation.AwaitingDocumentation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author LBuke (Teddeh)
 */
@AwaitingDocumentation
public final class RedisManager {

    private static final UUID UNIQUE_ID;

    static {
        UNIQUE_ID = UUID.randomUUID();
    }

    public static void enable(String... channels) {
        CompletableFuture.runAsync(() -> BaseRedisDatabase.getInstance().init(UNIQUE_ID, channels));
    }
}
