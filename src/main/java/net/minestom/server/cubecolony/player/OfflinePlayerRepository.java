package net.minestom.server.cubecolony.player;

import io.ebean.Database;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */
public class OfflinePlayerRepository {

    private final Database database;

    public OfflinePlayerRepository(Database database) {
        this.database = database;
    }

    public Optional<OfflinePlayer> findById(int id) {
        return Optional.ofNullable(database.find(OfflinePlayer.class, id));
    }

    public Optional<OfflinePlayer> findByUniqueId(UUID uniqueId) {
        return database.find(OfflinePlayer.class)
                .where()
                .eq("uniqueId", uniqueId)
                .findOneOrEmpty();
    }

    public Optional<OfflinePlayer> getByExactName(String name) {
        return database.find(OfflinePlayer.class)
                .where()
                .eq("name", name)
                .findOneOrEmpty();
    }

    public Optional<OfflinePlayer> getByName(String name) {
        return database.find(OfflinePlayer.class)
                .where()
                .like("name", name + "%")
                .findOneOrEmpty();
    }

    public void endSessions() {
        database.find(GameSession.class)
                .where()
                .isNull("ended_at")
                .findList()
                .forEach(session -> {
                    session.setEndDate(new Date());
                    database.save(session);
                });
        database.shutdown();
    }
}
