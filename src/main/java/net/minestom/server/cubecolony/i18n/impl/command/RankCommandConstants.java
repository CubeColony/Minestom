package net.minestom.server.cubecolony.i18n.impl.command;

import com.google.gson.annotations.SerializedName;

/**
 * @author LBuke (Teddeh)
 */
public final class RankCommandConstants {

    public String usage;

    @SerializedName("unrecognised_rank")
    public String unrecognisedRank;

    @SerializedName("received_rank")
    public String receivedRank;

    @SerializedName("revoked_rank")
    public String revokedRole;
}
