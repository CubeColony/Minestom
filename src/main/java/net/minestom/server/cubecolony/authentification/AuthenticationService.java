package net.minestom.server.cubecolony.authentification;

import com.cubecolony.api.players.CCPlayer;
import com.cubecolony.api.players.CCSession;
import io.ebean.Database;
import net.minestom.server.cubecolony.economy.game.BankAccount;
import net.minestom.server.cubecolony.economy.game.PlayerAccount;
import net.minestom.server.cubecolony.player.GameSession;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import net.minestom.server.cubecolony.player.PlayerPreferences;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.*;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
public class AuthenticationService {

    public final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final Database database;

    public AuthenticationService(Database database, ConnectionManager manager, GlobalEventHandler eventHandler) {
        this.database = database;
        manager.setPlayerProvider((uuid, username, connection) -> new Player(uuid, username, connection, findByUniqueId(uuid).orElseGet(() -> registerPlayer(uuid, username))));

        registerListeners(eventHandler);
    }

    /**
     * Register the event listeners nodes.
     */
    public void registerListeners(GlobalEventHandler handler) {
        EventNode<PlayerEvent> authNode = EventNode.type("authentication-listener", EventFilter.PLAYER);
        authNode.addListener(PlayerLoginEvent.class, event -> startSession(event.getPlayer()));
        authNode.addListener(PlayerDisconnectEvent.class, event -> endSession(event.getPlayer()));

        handler.addChild(authNode);
    }

    /**
     * Start a new session for the player
     *
     * @param player the player
     */
    public void startSession(@NotNull Player player) {
        final CCPlayer offlinePlayer = Objects.requireNonNull(player.getOfflinePlayer(), "OfflinePlayer is null");
        final PlayerConnection connection = Objects.requireNonNull(player.getPlayerConnection(), "PlayerConnection is null");

        SocketAddress socketAddress = connection.getRemoteAddress();
        String ip = "unknown";
        if (socketAddress instanceof InetSocketAddress) {
            InetAddress inetAddress = ((InetSocketAddress) socketAddress).getAddress();
            if (inetAddress instanceof Inet4Address || inetAddress instanceof Inet6Address)
                ip = inetAddress.getHostAddress();
        }

        GameSession session = new GameSession(ip,
                player.getLocale(),
                false,
                String.valueOf(connection.getProtocolVersion()),
                "unknown");
        database.insert(session);

        offlinePlayer.getSessions().add(session);
        database.update(offlinePlayer);
    }

    /**
     * Ends the current session of the player
     *
     * @param player the player
     */
    public void endSession(@NotNull Player player) {
        final CCPlayer offlinePlayer = Objects.requireNonNull(player.getOfflinePlayer(), "OfflinePlayer is null");
        final CCSession session = offlinePlayer.getCurrentSession();
        if (session == null) {
            LOGGER.warn("Player {} has no current session", player.getUsername());
            return;
        }
        session.setEndDate(new Date());
        database.update(session);
    }

    /**
     * Register a player in the database
     *
     * @param uuid the player's uuid
     * @param name the player's name
     * @return the registered player
     */
    private @Blocking OfflinePlayer registerPlayer(UUID uuid, String name) {
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

    public @Blocking Optional<OfflinePlayer> findByUniqueId(@NotNull UUID uuid) {
        return database.find(OfflinePlayer.class)
                .where()
                .eq("uuid", uuid)
                .findOneOrEmpty();
    }
}
