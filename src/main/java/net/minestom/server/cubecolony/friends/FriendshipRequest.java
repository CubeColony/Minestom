package net.minestom.server.cubecolony.friends;

import com.cubecolony.api.friends.CCFriendshipRequest;
import com.cubecolony.api.players.CCPlayer;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */
@Entity
@Table(name = "friendship_requests")
public class FriendshipRequest implements CCFriendshipRequest {

    @Id
    private long id;
    @OneToOne(targetEntity = OfflinePlayer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "issuer_id", nullable = false)
    private OfflinePlayer sender;
    @OneToOne(targetEntity = OfflinePlayer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "target_id", nullable = false)
    private OfflinePlayer receiver;
    @Column(name = "expires_at")
    private Date expirationDate;
    @Column
    private Status status;
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
    public @NotNull CCPlayer getSender() {
        return sender;
    }

    @Override
    public @NotNull CCPlayer getReceiver() {
        return receiver;
    }

    @Override
    public @NotNull Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(@NotNull Status status) {
        this.status = status;
    }

    @Override
    public boolean isExpired() {
        return expirationDate.after(new Date());
    }

    @Override
    public boolean isCompleted() {
        return status != Status.PENDING;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return this.createdAt;
    }

    @Override
    public @NotNull Date getExpirationDate() {
        return expirationDate;
    }

    @Override
    public @NotNull Date getLastUpdate() {
        return this.updatedAt;
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", expirationDate=" + expirationDate +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
