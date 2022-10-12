package net.minestom.server.cubecolony.economy.game;

import com.cubecolony.api.economy.game.CCBankAccount;
import com.cubecolony.api.economy.game.CCTransaction;
import com.cubecolony.api.players.CCPlayer;
import net.minestom.server.cubecolony.JPAModel;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@Entity
@Table(name = "bank_accounts")
public class BankAccount extends JPAModel implements CCBankAccount {

    @OneToOne
    private OfflinePlayer owner;
    @Column
    private double balance;
    @OneToMany(targetEntity = FinancialTransaction.class)
    private Set<CCTransaction> transactions;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull CCPlayer getOwner() {
        return owner;
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
}
