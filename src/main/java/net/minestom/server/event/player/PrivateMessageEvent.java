package net.minestom.server.event.player;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
public class PrivateMessageEvent implements Event, CancellableEvent {

    private Player sender;
    private Player receiver;
    private String message;
    private boolean cancelled;

    public PrivateMessageEvent(Player sender, Player receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
