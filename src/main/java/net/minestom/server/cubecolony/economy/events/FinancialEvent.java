package net.minestom.server.cubecolony.economy.events;

import com.cubecolony.api.economy.game.FinancialAccount;
import net.minestom.server.cubecolony.economy.game.FinancialTransaction;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 13/10/2022
 */
public interface FinancialEvent extends Event, CancellableEvent {

    @NotNull FinancialAccount getAccount();

    @NotNull String getReason();

    double getAmount();

    FinancialTransaction.Type getType();

    FinancialTransaction.Action getAction();
}
