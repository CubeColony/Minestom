package net.minestom.server.cubecolony.i18n.impl.command;

import com.google.gson.annotations.SerializedName;

/**
 * @author LBuke (Teddeh)
 */
public final class GiveCommandConstants {

    @SerializedName("usage")
    public String usage;

    @SerializedName("received_item")
    public String receivedItem;

    @Override
    public String toString() {
        return "GiveCommandConstants{" +
               "usage='" + this.usage + '\'' +
               ", receivedItem='" + this.receivedItem + '\'' +
               '}';
    }
}
