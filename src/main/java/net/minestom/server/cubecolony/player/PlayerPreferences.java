package net.minestom.server.cubecolony.player;

import com.cubecolony.api.players.CCPlayerPreferences;

import javax.persistence.*;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@Entity
@Table(name = "preferences")
public class PlayerPreferences implements CCPlayerPreferences {

    @Id
    private Long id;

    @Column(name = "chat")
    private boolean chatEnabled;
    @Column(name = "private_messages")
    private boolean privateMessagesEnabled;


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
}
