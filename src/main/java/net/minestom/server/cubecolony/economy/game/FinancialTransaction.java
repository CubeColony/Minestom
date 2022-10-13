package net.minestom.server.cubecolony.economy.game;

import com.cubecolony.api.economy.game.CCTransaction;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column
    private String description;
    @Column
    private double amount;
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

    public FinancialTransaction(String description, double amount, Type type, Action action) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.action = action;
    }

    public FinancialTransaction() {
    }

    @Override
    public long getId() {
        return id;
    }


    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public double getAmount() {
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
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", action=" + action +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
