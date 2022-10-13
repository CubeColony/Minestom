package net.minestom.server.cubecolony.ranks;

import com.cubecolony.api.ranks.CCRank;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
@Entity
@Table(name = "ranks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "prefix"})
})
public class Rank implements CCRank {

    @Id
    private long id;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "prefix", unique = true)
    private String prefix;
    @OneToOne(targetEntity = Rank.class)
    private CCRank child;
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> permissions;
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

    public Rank() {
    }

    public Rank(String name, String prefix, CCRank child, Set<String> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.child = child;
        this.permissions = permissions;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String s) {
        this.name = name;
    }

    @Override
    public @Nullable String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(@Nullable String s) {
        this.prefix = prefix;
    }

    @Override
    public @Nullable CCRank getChild() {
        return child;
    }

    @Override
    public void setChild(@Nullable CCRank ccRank) {
        this.child = ccRank;
    }

    @Override
    public @NotNull Set<String> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", child=" + child +
                ", permissions=" + permissions +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
