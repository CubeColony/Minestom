package net.minestom.server.cubecolony.economy.game;

import com.cubecolony.api.economy.game.CCBankAccount;
import com.cubecolony.api.economy.game.CCTransaction;
import com.cubecolony.api.players.CCPlayer;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
@Entity
@Table(name = "bank_accounts")
public class BankAccount implements CCBankAccount {

    @Id
    private long id;
    @Column
    private double balance;
    @OneToMany(targetEntity = FinancialTransaction.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "bank_account_transactions", joinColumns = @JoinColumn(name = "bank_account_id"), inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    private Set<CCTransaction> transactions;
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

    public BankAccount(double balance) {
        this.balance = balance;
        this.transactions = new HashSet<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public BankAccount() {
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double v) {
        this.balance = v;
    }

    @Override
    public void deposit(double v, @NotNull String s) {
        MinecraftServer.getEconomyService().deposit(this, v, s, CCTransaction.Type.BANK);
    }

    @Override
    public boolean withdraw(double v, @NotNull String s) {
        return withdrawAsync(v, s).join();
    }

    @Override
    public CompletableFuture<Boolean> withdrawAsync(double v, @NotNull String s) {
        return MinecraftServer.getEconomyService().withdraw(this, v, s, CCTransaction.Type.BANK)
                .exceptionally(e -> {
                    MinecraftServer.getExceptionManager().handleException(e);
                    return false;
                });
    }

    @Override
    public boolean transfer(@NotNull CCPlayer ccPlayer, double v, @NotNull String s) {
        return transferAsync(ccPlayer, v, s).join();
    }

    @Override
    public CompletableFuture<Boolean> transferAsync(@NotNull CCPlayer ccPlayer, double v, @NotNull String s) {
        return MinecraftServer.getEconomyService().transfer(this, ccPlayer.getBankAccount(), v, s, CCTransaction.Type.BANK)
                .exceptionally(e -> {
                    MinecraftServer.getExceptionManager().handleException(e);
                    return false;
                });
    }

    @Override
    public @NotNull Set<CCTransaction> getTransactions() {
        return transactions;
    }

    @Override
    public @NotNull Date getUpdateDate() {
        return this.updatedAt;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return this.createdAt;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", balance=" + balance +
                ", transactions=" + transactions +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
