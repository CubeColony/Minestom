package net.minestom.server.cubecolony.i18n.impl.command;

import com.google.gson.annotations.SerializedName;

/**
 * @author LBuke (Teddeh)
 */
public final class KillCommandConstants {

    @SerializedName("usage")
    public String usage;

    @SerializedName("killed_entities")
    public String killedEntities;

    @Override
    public String toString() {
        return "KillCommandConstants{" +
               "usage='" + this.usage + '\'' +
               ", killedEntities='" + this.killedEntities + '\'' +
               '}';
    }
}
