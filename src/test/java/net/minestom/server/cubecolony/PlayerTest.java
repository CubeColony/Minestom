package net.minestom.server.cubecolony;

import com.cubecolony.api.brainstorm.player.rank.RankData;
import com.cubecolony.api.brainstorm.player.rank.WrappedRank;
import net.minestom.server.entity.Player;

/**
 * @author LBuke (Teddeh)
 */
public final class PlayerTest {

    @SuppressWarnings("ConstantConditions")
    private void example() {
        Player player = null;

        final long snowflake = player.snowflake();

        // Check if player has rank by id
        int ownerRankId = 1; // <--- temp
        if (player.rankAdapter().has(ownerRankId)) {
            // do something
        }

        // Primary rank is the rank with the highest weight (that the player has)
        final RankData primary = player.rankAdapter().primary();
        long primaryRankExpiresAt = primary.expiresAt();
        long primaryRankAcquiredAt = primary.acquiredAt();
        long primaryRankId = primary.rank().id();
        String primaryRankName = primary.rank().name();
        int primaryRankWeight = primary.rank().weight();

        player.rankAdapter().add(new RankData(null, 0, 0));
        player.rankAdapter().remove(ownerRankId);

        for (RankData data : player.rankAdapter().ranks()) {
            WrappedRank rank = data.rank();
            // do stuff
        }
    }
}
