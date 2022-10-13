package net.minestom.server.cubecolony.i18n.impl.command;

import com.google.gson.annotations.SerializedName;

/**
 * @author LBuke (Teddeh)
 */
public final class GamemodeCommandConstants {

    @SerializedName("gamemode_usage")
    public String usage;

    @SerializedName("invalid_gamemode")
    public String invalidGamemode;

    @SerializedName("gamemode_changed")
    public String gamemodeChanged;

    @SerializedName("gamemode_success_self")
    public String gamemodeSuccessSelf;

    @SerializedName("gamemode_success_other")
    public String gamemodeSuccessOther;

    public String survival;
    public String creative;
    public String adventure;
    public String spectator;

    @Override
    public String toString() {
        return "GamemodeCommandConstants{" +
               "usage='" + this.usage + '\'' +
               ", invalidGamemode='" + this.invalidGamemode + '\'' +
               ", gamemodeChanged='" + this.gamemodeChanged + '\'' +
               ", gamemodeSuccessSelf='" + this.gamemodeSuccessSelf + '\'' +
               ", gamemodeSuccessOther='" + this.gamemodeSuccessOther + '\'' +
               ", survival='" + this.survival + '\'' +
               ", creative='" + this.creative + '\'' +
               ", adventure='" + this.adventure + '\'' +
               ", spectator='" + this.spectator + '\'' +
               '}';
    }
}
