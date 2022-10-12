package net.minestom.server.cubecolony.friends;

import com.cubecolony.api.friends.CCFriendshipRequest;
import com.cubecolony.api.players.CCPlayer;
import net.minestom.server.cubecolony.JPAModel;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@Entity
@Table(name = "friendship_requests")
public class FriendshipRequest extends JPAModel implements CCFriendshipRequest {

    @OneToOne
    private OfflinePlayer sender;
    @OneToOne
    private OfflinePlayer receiver;
    @Column(name = "expires_at")
    private Date expirationDate;
    @Column
    private Status status;

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
}
