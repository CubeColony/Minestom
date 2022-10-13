package net.minestom.server.cubecolony.economy.store;

import com.cubecolony.api.economy.store.CCProduct;
import com.cubecolony.api.economy.store.CCPurchase;
import com.cubecolony.api.players.CCPlayer;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */
@Entity
@Table(name = "purchases")
public class Purchase implements CCPurchase {

    @Id
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    private Product product;
    @Column
    private int amount;
    @Column
    private double price;
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull CCPlayer getPlayer() {
        return null;
    }

    @Override
    public @NotNull CCProduct getProduct() {
        return product;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return this.createdAt;
    }

    @Override
    public @NotNull Date getUpdateDate() {
        return this.updatedAt;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", product=" + product +
                ", amount=" + amount +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
