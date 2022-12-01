package com.cubecolony.server;

import java.time.Instant;

/**
 * @author LBuke (Teddeh)
 */
public final class DiscordTest {

    public static void main(String[] args) {
//        final long user_id = 360501297658724353L;
//        final Instant timeCreated = Instant.ofEpochMilli((user_id >> 22) + 1420070400000L);
//        final long internalWorkerId = (user_id & 0x3E0000) >> 17;
//        final long internalProcessId = (user_id & 0x1F000) >> 12;
//        final long increment = user_id & 0xFFF;
//
//        System.out.printf("user_id: %s%n", user_id);
//        System.out.printf("timeCreated: %s%n", timeCreated);
//        System.out.printf("internalWorkerId: %s%n", internalWorkerId);
//        System.out.printf("internalProcessId: %s%n", internalProcessId);
//        System.out.printf("increment: %s%n", increment);

        final long now = System.currentTimeMillis();

        final long discord_id = 1;
        final long website_id = 2;

        long snowflake = now << 22 | discord_id << 17 | website_id << 12;
        System.out.printf("Snowflake: %s%n", snowflake);

        final Instant timeCreated = Instant.ofEpochMilli((snowflake >> 22));
        System.out.printf("TimeCreated: %s%n", timeCreated);
        System.out.printf("DiscordId: %s%n", discord_id);
        System.out.printf("WebsiteId: %s%n", website_id);
    }
}
