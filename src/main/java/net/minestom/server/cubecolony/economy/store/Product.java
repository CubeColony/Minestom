package net.minestom.server.cubecolony.economy.store;

import com.cubecolony.api.economy.store.CCProduct;
import net.minestom.server.cubecolony.JPAModel;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@Entity
@Table(name = "products")
public class Product extends JPAModel implements CCProduct {

    @Column(unique = true)
    private String name;
    @Column
    private String payload;
    @Column
    private PayloadType type;
    @Column
    private String description;
    @Column
    private double price;
    @Column
    private int stock;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getPayload() {
        return payload;
    }

    @Override
    public @NotNull PayloadType getPayloadType() {
        return type;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public void setDescription(@NotNull String s) {
        this.description = s;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double v) {
        this.price = v;
    }

    @Override
    public int getStock() {
        return stock;
    }

    @Override
    public void setStock(int i) {
        this.stock = i;
    }

    @Override
    public @NotNull Date getUpdateDate() {
        return updatedAt;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return createdAt;
    }
}
