package net.minestom.server.cubecolony.brainstorm;

import com.cubecolony.api.brainstorm.adapter.Adaptor;
import com.cubecolony.api.brainstorm.player.CPlayer;
import com.cubecolony.api.brainstorm.player.rank.RankAdapter;
import com.cubecolony.api.brainstorm.player.rank.RankData;
import com.cubecolony.api.brainstorm.player.rank.WrappedRank;
import com.cubecolony.redis.Redisable;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * @author LBuke (Teddeh)
 */
public final class BasePlayer implements CPlayer, Redisable {

    @SerializedName("adaptors")
    private final Map<Class<? extends Adaptor<?>>, Adaptor<?>> adaptors = Maps.newConcurrentMap();

    @SerializedName("snowflake") long snowflake;
    @SerializedName("uuid")     UUID uniqueId;
    @SerializedName("username") private String username;

    public BasePlayer() {}

    public BasePlayer(@NotNull UUID uniqueId, @NotNull String username) {
        this.uniqueId = uniqueId;
        this.username = username;

        final RankAdapter rankAdapter = new RankAdapter();

        final int id = rankAdapter.ranks().size() + 1;
        rankAdapter.add(new RankData(new WrappedRank(id, "test-" + id, "", 1), 0, 0));

        this.add(rankAdapter);
    }

    @Override
    public long snowflake() {
        return this.snowflake;
    }

    @Override
    public @NotNull UUID uniqueId() {
        return this.uniqueId;
    }

    @Override
    public @NotNull String username() {
        return this.username;
    }

    @Override
    public void username(@NotNull String username) {
        this.username = username;
    }

    @Override
    public @NotNull Map<Class<? extends Adaptor<?>>, Adaptor<?>> adaptors() {
        return this.adaptors;
    }
}
