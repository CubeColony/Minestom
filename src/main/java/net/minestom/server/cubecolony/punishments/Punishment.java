package net.minestom.server.cubecolony.punishments;

import com.cubecolony.api.players.CCPlayer;
import com.cubecolony.api.punishments.CCPunishment;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */
@Entity
@Table(name = "punishments")
public class Punishment implements CCPunishment {

    @Id
    private long id;
    @OneToOne
    private OfflinePlayer player;
    @Column
    private String issuer;
    @Column
    private PunishmentType type;
    @Column
    private String reason;
    @Column(name = "expires_at")
    private Date expiration;
    @Column
    private boolean permanent;
    @Column
    private boolean revoked;
    @Column
    private String revoker;
    @Column(name = "revocation_reason")
    private String revocationReason;
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull CCPlayer getPlayer() {
        return player;
    }

    @Override
    public @NotNull String getIssuer() {
        return issuer;
    }

    @Override
    public @NotNull PunishmentType getType() {
        return type;
    }

    @Override
    public @NotNull String getReason() {
        return reason;
    }

    @Override
    public boolean isExpired() {
        return getExpirationDate().after(new Date());
    }

    @Override
    public boolean isPermanent() {
        return permanent;
    }

    @Override
    public boolean isRevoked() {
        return revoked;
    }

    @Override
    public @Nullable String getRevoker() {
        return revoker;
    }

    @Override
    public @Nullable String getRevocationReason() {
        return revocationReason;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return this.createdAt;
    }

    @Override
    public @NotNull Date getExpirationDate() {
        return expiration;
    }

    @Override
    public @NotNull Date getLastUpdateDate() {
        return this.updatedAt;
    }

    @Override
    public String toString() {
        return "Punishment{" +
                "id=" + id +
                ", player=" + player +
                ", issuer='" + issuer + '\'' +
                ", type=" + type +
                ", reason='" + reason + '\'' +
                ", expiration=" + expiration +
                ", permanent=" + permanent +
                ", revoked=" + revoked +
                ", revoker='" + revoker + '\'' +
                ", revocationReason='" + revocationReason + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
