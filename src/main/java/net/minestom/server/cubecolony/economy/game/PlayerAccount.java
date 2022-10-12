package net.minestom.server.cubecolony.economy.game;

import com.cubecolony.api.economy.game.CCPlayerAccount;
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
@Table(name = "player_accounts")
public class PlayerAccount extends JPAModel implements CCPlayerAccount {

    @OneToOne
    private OfflinePlayer player;
    @Column
    private double balance;
    @OneToMany(targetEntity = FinancialTransaction.class)

    private Set<CCTransaction> transactions;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull CCPlayer getPlayer() {
        return player;
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
    public @NotNull Date getLastUpdateDate() {
        return this.updatedAt;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return this.createdAt;
    }
}
