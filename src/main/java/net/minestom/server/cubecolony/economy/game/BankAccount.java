package net.minestom.server.cubecolony.economy.game;

import com.cubecolony.api.economy.game.CCBankAccount;
import com.cubecolony.api.economy.game.CCTransaction;
import com.cubecolony.api.players.CCPlayer;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    @OneToMany(targetEntity = FinancialTransaction.class)
    private Set<CCTransaction> transactions;
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

    public BankAccount(double balance, Set<CCTransaction> transactions) {
        this.balance = balance;
        this.transactions = transactions;
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
