package net.minestom.server.cubecolony.player;

import com.cubecolony.api.players.CCPlayerPreferences;

import javax.persistence.*;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */
@Entity
@Table(name = "preferences")
public class PlayerPreferences implements CCPlayerPreferences {

    @Id
    private long id;

    @Column(name = "chat")
    private boolean chatEnabled;
    @Column(name = "private_messages")
    private boolean privateMessagesEnabled;
    @Column(name = "friend_requests")
    private boolean friendRequestsEnabled;


    public PlayerPreferences(boolean chatEnabled, boolean privateMessagesEnabled, boolean friendRequestsEnabled) {
        this.chatEnabled = chatEnabled;
        this.privateMessagesEnabled = privateMessagesEnabled;
        this.friendRequestsEnabled = friendRequestsEnabled;
    }

    public PlayerPreferences() {
    }

    @Override
    public boolean arePrivateMessagesEnabled() {
        return privateMessagesEnabled;
    }

    @Override
    public void setPrivateMessagesEnabled(boolean b) {
        this.privateMessagesEnabled = b;
    }

    @Override
    public boolean isChatEnabled() {
        return chatEnabled;
    }

    @Override
    public void setChatEnabled(boolean b) {
        this.chatEnabled = b;
    }

    @Override
    public boolean acceptsFriendRequests() {
        return friendRequestsEnabled;
    }

    @Override
    public void setAcceptsFriendRequests(boolean b) {
        this.friendRequestsEnabled = b;
    }

    @Override
    public String toString() {
        return "PlayerPreferences{" +
                "id=" + id +
                ", chatEnabled=" + chatEnabled +
                ", privateMessagesEnabled=" + privateMessagesEnabled +
                '}';
    }
}
