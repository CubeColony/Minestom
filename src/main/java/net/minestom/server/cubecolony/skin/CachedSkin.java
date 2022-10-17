package net.minestom.server.cubecolony.skin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
@Entity
@Table(name = "cached_skins")
public class CachedSkin {

    @Id
    private long id;

    @Column(nullable = false)
    private int hash;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private String signature;

    public CachedSkin(int hash, String value, String signature) {
        this.hash = hash;
        this.value = value;
        this.signature = signature;
    }

    public CachedSkin() {
    }

    public long getId() {
        return id;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
