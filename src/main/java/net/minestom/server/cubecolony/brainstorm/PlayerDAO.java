package net.minestom.server.cubecolony.brainstorm;

import com.cubecolony.api.annotation.Documented;
import com.cubecolony.api.util.UUIDUtil;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author LBuke (Teddeh)
 */
@Documented
@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public final class PlayerDAO {

    /**
     * It fetches a player's snowflake from the database, given their username.
     *
     * @param connection The connection to the database.
     * @param player The player to fetch data for.
     * @return true if exists in the database.
     */
    @Blocking
    public static boolean fetchByUniqueId(@NotNull Connection connection, @NotNull BasePlayer player) {
        @Language("SQL")
        final String query = """
                SELECT `id` \
                FROM `account` \
                WHERE `uuid`=UNHEX(?);
                """;
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, UUIDUtil.strip(player.uniqueId()));
            try (final ResultSet result = statement.executeQuery()) {
                if (!result.next())
                    return false;

                player.snowflake = result.getLong("id");
                return true;
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * It fetches a player's snowflake and unique ID from the database,
     * given their username.
     *
     * @param connection The connection to the database.
     * @param player The player to fetch data for.
     * @return true if exists in the database.
     */
    @Blocking
    public static boolean fetchByUsername(@NotNull Connection connection, @NotNull BasePlayer player) {
        @Language("SQL")
        final String query = """
                SELECT `id`, HEX(`uuid`) AS `unique_id` \
                FROM `account` \
                WHERE LOWER(`username`)=LOWER(?);
                """;
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.username());
            try (final ResultSet result = statement.executeQuery()) {
                if (!result.next())
                    return false;

                player.snowflake = result.getLong("id");
                player.uniqueId = UUIDUtil.build(result.getString("unique_id"));
                return true;
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * It inserts a new row into the `account` table if the player doesn't exist,
     * or updates the player's username if they do.
     *
     * @param connection The connection to the database.
     * @param player The player to update.
     */
    @Blocking
    public static void update(@NotNull Connection connection, @NotNull BasePlayer player) {
        final long snowflake = (player.snowflake() == 0L ? System.currentTimeMillis() << 22 : player.snowflake);

        @Language("SQL")
        final String query = """
            INSERT INTO `account` \
            (`id`, `username`, `uuid`) \
            VALUES (?, ?, UNHEX(?)) \
            ON DUPLICATE KEY UPDATE `username`=?;
            """;
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, snowflake);
            statement.setString(2, player.username());
            statement.setString(3, UUIDUtil.strip(player.uniqueId()));
            statement.setString(4, player.username());
            statement.execute();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
