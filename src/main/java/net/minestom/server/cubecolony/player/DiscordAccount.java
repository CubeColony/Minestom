package net.minestom.server.cubecolony.player;

import com.cubecolony.api.players.CCDiscordAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */
@Entity
@Table(name = "discord_accounts")
public class DiscordAccount implements CCDiscordAccount {

    @Id
    private long id;

    @Column(name = "discord_id", unique = true, nullable = false)
    private long discordId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    public DiscordAccount() {
    }

    public DiscordAccount(long discordId, String username) {
        this.discordId = discordId;
        this.username = username;
    }

    @Override
    public long getId() {
        return id;
    }

    public long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
