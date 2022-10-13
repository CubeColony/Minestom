package net.minestom.server.cubecolony.i18n.impl.command;

import com.google.gson.annotations.SerializedName;

/**
 * @author LBuke (Teddeh)
 */
public final class TeleportCommandConstants {

    @SerializedName("usage")
    public String usage;

    @SerializedName("teleported_to_target")
    public String teleportedToTarget;

    @SerializedName("teleported_to_position")
    public String teleportedToPosition;

    @Override
    public String toString() {
        return "TeleportCommandConstants{" +
               "usage='" + this.usage + '\'' +
               ", teleportedToTarget='" + this.teleportedToTarget + '\'' +
               ", teleportedToPosition='" + this.teleportedToPosition + '\'' +
               '}';
    }
}
