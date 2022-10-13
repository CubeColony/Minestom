package net.minestom.server.cubecolony.authentification;

import com.cubecolony.api.players.CCPlayer;
import io.ebean.Database;
import net.minestom.server.cubecolony.economy.game.BankAccount;
import net.minestom.server.cubecolony.economy.game.PlayerAccount;
import net.minestom.server.cubecolony.player.GameSession;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import net.minestom.server.cubecolony.player.PlayerPreferences;
import net.minestom.server.entity.Player;
import net.minestom.server.network.ConnectionManager;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
public class AuthenticationService {

    public final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final Database database;

    public AuthenticationService(Database database, ConnectionManager manager) {
        this.database = database;

        manager.setPlayerProvider((uuid, username, connection) -> {
            CCPlayer player = findByUniqueId(uuid)
                    .orElseGet(() -> registerPlayer(uuid, username));
            GameSession session = new GameSession(connection.getRemoteAddress().toString(),
                    Locale.ENGLISH, // TODO: Get locale from client
                    false,
                    String.valueOf(connection.getProtocolVersion()),
                    "1.0");
            database.insert(session);
            player.getSessions().add(session);
            database.update(player);

            return new Player(uuid, username, connection, player);
        });
    }

    private @Blocking CCPlayer registerPlayer(UUID uuid, String name) {
        final Date now = new Date();
        PlayerPreferences preferences = new PlayerPreferences(true, true, true);
        database.insert(preferences);

        BankAccount bankAccount = new BankAccount(0, new HashSet<>());
        database.insert(bankAccount);

        PlayerAccount account = new PlayerAccount(0, new HashSet<>());
        database.insert(account);

        OfflinePlayer offlinePlayer = new OfflinePlayer(uuid,
                name,
                Locale.ENGLISH,
                0,
                now,
                preferences,
                bankAccount,
                account,
                null,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>());
        database.save(offlinePlayer);

        return offlinePlayer;
    }

    public @Blocking Optional<CCPlayer> findByUniqueId(@NotNull UUID uuid) {
        return database.find(CCPlayer.class)
                .where()
                .eq("uuid", uuid)
                .findOneOrEmpty();
    }
}
