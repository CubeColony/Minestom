package net.minestom.server.cubecolony.economy.store;

import com.cubecolony.api.economy.store.CCProduct;
import com.cubecolony.api.economy.store.CCPurchase;
import com.cubecolony.api.players.CCPlayer;
import net.minestom.server.cubecolony.JPAModel;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@Entity
@Table(name = "purchases")
public class Purchase extends JPAModel implements CCPurchase {

    @OneToOne(cascade = CascadeType.ALL)
    private OfflinePlayer player;
    @OneToOne(cascade = CascadeType.ALL)
    private Product product;
    @Column
    private int amount;
    @Column
    private double price;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull CCPlayer getPlayer() {
        return player;
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

}
