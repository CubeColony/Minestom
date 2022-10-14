package net.minestom.server.cubecolony.redis.message;

import net.minestom.server.cubecolony.redis.Redisable;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */
public class DiscordOAuthMessage implements Redisable {

    private String code;
    private String state;

    public DiscordOAuthMessage() {
    }

    public DiscordOAuthMessage(String code, String state) {
        this.code = code;
        this.state = state;
    }

    public String getCode() {
        return this.code;
    }

    public String getState() {
        return this.state;
    }
}
