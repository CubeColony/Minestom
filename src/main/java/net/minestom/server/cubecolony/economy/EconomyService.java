package net.minestom.server.cubecolony.economy;

import com.cubecolony.api.economy.game.CCTransaction;
import com.cubecolony.api.economy.game.FinancialAccount;
import io.ebean.Database;
import net.minestom.server.cubecolony.economy.events.DepositEvent;
import net.minestom.server.cubecolony.economy.events.WithdrawEvent;
import net.minestom.server.cubecolony.economy.game.FinancialTransaction;
import net.minestom.server.event.GlobalEventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 13/10/2022
 */
public class EconomyService {

    private final Database database;
    private final GlobalEventHandler globalEventHandler;

    public EconomyService(Database database, GlobalEventHandler globalEventHandler) {
        this.database = database;
        this.globalEventHandler = globalEventHandler;
    }

    public void deposit(@NotNull FinancialAccount account, double amount, @NotNull String reason, FinancialTransaction.Type type) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be positive");
        globalEventHandler.callCancellable(new DepositEvent(account, reason, amount, type, CCTransaction.Action.DEPOSIT), () -> {
            FinancialTransaction transaction = new FinancialTransaction(reason, amount, type, FinancialTransaction.Action.DEPOSIT);
            database.insert(transaction);

            account.setBalance(account.getBalance() + amount);

            account.getTransactions().add(transaction);
            database.update(account);
        });
    }

    public CompletableFuture<Boolean> withdraw(@NotNull FinancialAccount account, double amount, @NotNull String reason, FinancialTransaction.Type type) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        globalEventHandler.callCancellable(new WithdrawEvent(account, reason, amount, type, CCTransaction.Action.WITHDRAW), () -> {
            try {
                if (amount < 0)
                    throw new IllegalArgumentException("Amount must be positive");
                if (account.getBalance() < amount) {
                    future.complete(false);
                    return;
                }
                FinancialTransaction transaction = new FinancialTransaction(reason, amount, type, FinancialTransaction.Action.WITHDRAW);
                database.insert(transaction);

                account.setBalance(account.getBalance() - amount);

                account.getTransactions().add(transaction);
                database.update(account);
                future.complete(true);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public CompletableFuture<Boolean> transfer(@NotNull FinancialAccount sender, @NotNull FinancialAccount receiver, double amount, @NotNull String reason, FinancialTransaction.Type type) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        globalEventHandler.callCancellable(new WithdrawEvent(sender, reason, amount, type, CCTransaction.Action.DEPOSIT), () -> {
            try {
                if (amount < 0)
                    throw new IllegalArgumentException("Amount must be positive");
                if (sender.getBalance() < amount) {
                    future.complete(false);
                    return;
                }
                FinancialTransaction withdrawTransaction = new FinancialTransaction(reason, amount, type, FinancialTransaction.Action.WITHDRAW);
                database.insert(withdrawTransaction);

                sender.getTransactions().add(withdrawTransaction);
                database.update(sender);

                sender.setBalance(sender.getBalance() - amount);

                FinancialTransaction depositTransaction = new FinancialTransaction(reason, amount, type, FinancialTransaction.Action.DEPOSIT);
                database.insert(depositTransaction);

                receiver.setBalance(receiver.getBalance() + amount);

                receiver.getTransactions().add(depositTransaction);
                database.update(receiver);
                future.complete(true);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }
}
