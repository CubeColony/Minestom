package net.minestom.server.cubecolony.ranks;

import com.cubecolony.api.ranks.CCRank;
import net.minestom.server.cubecolony.JPAModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Set;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@Entity
@Table(name = "ranks")
public class Rank extends JPAModel implements CCRank {

    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "prefix", unique = true)
    private String prefix;
    @OneToOne(targetEntity = Rank.class)
    private CCRank child;
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> permissions;


    @Override
    public long getId() {
        return this.id;
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
}
