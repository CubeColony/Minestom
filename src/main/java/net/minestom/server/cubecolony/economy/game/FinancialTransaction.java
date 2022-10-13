package net.minestom.server.cubecolony.economy.game;

import com.cubecolony.api.economy.game.CCBankAccount;
import com.cubecolony.api.economy.game.CCTransaction;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */

@Entity
@Table(name = "transactions")
public class FinancialTransaction  implements CCTransaction {

    @Id
    private long id;
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
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

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

    @Override
    public String toString() {
        return "FinancialTransaction{" +
                "id=" + id +
                ", account=" + account +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", action=" + action +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
