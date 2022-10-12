package net.minestom.server.cubecolony.economy.game;

import com.cubecolony.api.economy.game.CCBankAccount;
import com.cubecolony.api.economy.game.CCTransaction;
import net.minestom.server.cubecolony.JPAModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */

@Entity
@Table(name = "transactions")
public class FinancialTransaction extends JPAModel implements CCTransaction {

    @OneToOne
    private BankAccount account;
    @Column
    private String description;
    @Column
    private float amount;
    @Column
    private Type type;
    @Column
    private Action action;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public @NotNull CCBankAccount getAccount() {
        return account;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public @NotNull Type getType() {
        return type;
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public @NotNull Date getLastUpdate() {
        return this.updatedAt;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return this.createdAt;
    }
}
