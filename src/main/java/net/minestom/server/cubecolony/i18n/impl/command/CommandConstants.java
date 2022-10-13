package net.minestom.server.cubecolony.i18n.impl.command;

import com.google.gson.annotations.SerializedName;

/**
 * @author LBuke (Teddeh)
 */
public final class CommandConstants {

    @SerializedName("unknown_command")
    public String unknownCommand;

    @SerializedName("player_command_only")
    public String playerCommandOnly;

    @SerializedName("requires_rank")
    public String requiresRank;

    @SerializedName("target_not_found")
    public String targetNotFound;

    @SerializedName("entity_not_found")
    public String entityNotFound;

    public WhitelistCommandConstants whitelist;
    public GamemodeCommandConstants gamemode;
    public GiveCommandConstants give;
    public KillCommandConstants kill;
    public TeleportCommandConstants teleport;
    public RankCommandConstants rank;
    public DiscordCommandConstants discord;

    @Override
    public String toString() {
        return "CommandConstants{" +
               "unknownCommand='" + this.unknownCommand + '\'' +
               ", playerCommandOnly='" + this.playerCommandOnly + '\'' +
               ", requiresRank='" + this.requiresRank + '\'' +
               ", targetNotFound='" + this.targetNotFound + '\'' +
               ", entityNotFound='" + this.entityNotFound + '\'' +
               ", whitelist=" + this.whitelist +
               ", gamemode=" + this.gamemode +
               ", give=" + this.give +
               ", kill=" + this.kill +
               ", teleport=" + this.teleport +
               ", rank=" + this.rank +
               '}';
    }
}
