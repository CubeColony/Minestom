package net.minestom.server.cubecolony.chat;

import com.cubecolony.api.chat.CCPrivateMessage;
import com.cubecolony.api.players.CCPlayer;
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
public class PrivateMessage implements CCPrivateMessage {

    @Id
    private long id;
    @ManyToOne(targetEntity = OfflinePlayer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CCPlayer sender;
    @ManyToOne(targetEntity = OfflinePlayer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CCPlayer receiver;
    @Column(nullable = false)
    private String message;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public PrivateMessage() {
    }

    public PrivateMessage(CCPlayer sender, CCPlayer receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = new Date();
    }

    @Override
    public long getId() {
        return id;
    }

    @NotNull
    @Override
    public CCPlayer getSender() {
        return sender;
    }

    @NotNull
    @Override
    public CCPlayer getReceiver() {
        return receiver;
    }

    @NotNull
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return createdAt;
    }
}
