package net.minestom.server.cubecolony.economy.events;

import com.cubecolony.api.economy.game.FinancialAccount;
import net.minestom.server.cubecolony.economy.game.FinancialTransaction;
import org.jetbrains.annotations.NotNull;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 13/10/2022
 */
public class WithdrawEvent implements FinancialEvent {

    private final FinancialAccount account;
    private final String reason;
    private final double amount;
    private final FinancialTransaction.Type type;
    private final FinancialTransaction.Action action;
    private boolean cancelled;

    public WithdrawEvent(FinancialAccount account, String reason, double amount, FinancialTransaction.Type type, FinancialTransaction.Action action) {
        this.account = account;
        this.reason = reason;
        this.amount = amount;
        this.type = type;
        this.action = action;
    }

    @Override
    public @NotNull FinancialAccount getAccount() {
        return account;
    }

    @Override
    public @NotNull String getReason() {
        return reason;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public FinancialTransaction.Type getType() {
        return type;
    }

    @Override
    public FinancialTransaction.Action getAction() {
        return action;
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
