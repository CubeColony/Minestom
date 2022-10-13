package net.minestom.server.cubecolony;

import io.ebean.DB;
import io.ebean.Database;
import net.minestom.server.cubecolony.economy.game.BankAccount;
import net.minestom.server.cubecolony.economy.game.PlayerAccount;
import net.minestom.server.cubecolony.player.OfflinePlayer;
import net.minestom.server.cubecolony.player.PlayerPreferences;
import net.minestom.server.cubecolony.ranks.Rank;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 13/10/2022
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    static Database database;

    @BeforeAll
    static void beforeAll() {
        database = DB.getDefault();
        Assertions.assertNotNull(database);
    }

    @Test
    @Order(1)
    void insertAndFindRank() {
        Rank rank = new Rank("test0", "test0", null, Set.of("test.permission1", "test.permission2"));
        database.save(rank);

        Rank found = database.find(Rank.class, 1);
        Assertions.assertNotNull(found);
    }

    @Test
    @Order(2)
    void insertQueryDeleteRankWithChild() {
        Rank child = new Rank("child", "child", null, Set.of("child.permission1", "child.permission2"));
        database.save(child);

        Rank rank = new Rank("test", "test", child, Set.of("test.permission1", "test.permission2"));
        database.save(rank);

        Rank found = database.find(Rank.class, 3);
        Assertions.assertNotNull(found);
        Assertions.assertNotNull(found.getChild());

        database.delete(rank);
        found = database.find(Rank.class, 3);
        Assertions.assertNull(found);

        database.delete(child);
        found = database.find(Rank.class, 2);
        Assertions.assertNull(found);
    }

    @Test
    @Order(3)
    void insertAndQueryPlayerPreferences() {
        PlayerPreferences preferences = new PlayerPreferences(true, false, true);
        database.insert(preferences);

        PlayerPreferences found = database.find(PlayerPreferences.class, 1);
        Assertions.assertNotNull(found);
    }

    @Test
    @Order(4)
    void insertAndQueryBankAccount() {
        BankAccount bankAccount = new BankAccount(new Random().nextDouble(), new HashSet<>());
        database.insert(bankAccount);

        BankAccount found = database.find(BankAccount.class, 1);
        Assertions.assertNotNull(found);
    }

    @Test
    @Order(5)
    void insertAndQueryPlayerAccount() {
        PlayerAccount playerAccount = new PlayerAccount(new Random().nextDouble(), new HashSet<>());
        database.insert(playerAccount);

        PlayerAccount found = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(found);
    }

    @Test
    @Order(6)
    void insertAndFindPlayer() {
        final UUID uuid = UUID.randomUUID();
        final String name = "test";
        final Locale locale = Locale.ENGLISH;
        final long playTime = new Random().nextLong();
        final Date lastLogin = new Date();

        // Rank
        Rank rank = database.find(Rank.class, 1);
        Assertions.assertNotNull(rank);

        // Preferences
        PlayerPreferences preferences = database.find(PlayerPreferences.class, 1);
        Assertions.assertNotNull(preferences);

        // Bank account
        BankAccount bankAccount = database.find(BankAccount.class, 1);
        Assertions.assertNotNull(bankAccount);

        // Player account
        PlayerAccount playerAccount = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(playerAccount);

        // Player
        OfflinePlayer player = new OfflinePlayer(uuid, name, locale, playTime, lastLogin, preferences, bankAccount, playerAccount, rank, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        database.insert(player);

        OfflinePlayer found = database.find(OfflinePlayer.class, 1);
        Assertions.assertNotNull(found);

        // Print all players variables
        System.out.println(player);
    }

    @AfterAll
    static void afterAll() {
        database.delete(PlayerAccount.class, 1);
        database.delete(BankAccount.class, 1);
        database.delete(PlayerPreferences.class, 1);
        database.delete(OfflinePlayer.class, 1);
        database.delete(Rank.class, 1);

        database.shutdown(true, false);
        database = null;
    }
}
