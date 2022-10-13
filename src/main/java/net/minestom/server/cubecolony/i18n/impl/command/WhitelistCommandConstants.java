package net.minestom.server.cubecolony.i18n.impl.command;

import com.google.gson.annotations.SerializedName;

/**
 * @author LBuke (Teddeh)
 */
public final class WhitelistCommandConstants {

    @SerializedName("usage")
    public String usage;

    @SerializedName("whitelist_on")
    public String on;

    @SerializedName("whitelist_off")
    public String off;

    @SerializedName("whitelist_added_user")
    public String addedUser;

    @SerializedName("whitelist_removed_user")
    public String removedUser;

    @SerializedName("not_whitelisted")
    public String notWhitelisted;

    @Override
    public String toString() {
        return "WhitelistCommandConstants{" +
               "usage='" + this.usage + '\'' +
               ", on='" + this.on + '\'' +
               ", off='" + this.off + '\'' +
               ", addedUser='" + this.addedUser + '\'' +
               ", removedUser='" + this.removedUser + '\'' +
               ", notWhitelisted='" + this.notWhitelisted + '\'' +
               '}';
    }
}
