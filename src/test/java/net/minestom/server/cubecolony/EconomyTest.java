package net.minestom.server.cubecolony;

import com.cubecolony.api.economy.game.CCTransaction;
import io.ebean.DB;
import io.ebean.Database;
import net.minestom.server.cubecolony.economy.EconomyService;
import net.minestom.server.cubecolony.economy.game.BankAccount;
import net.minestom.server.cubecolony.economy.game.PlayerAccount;
import net.minestom.server.event.GlobalEventHandler;
import org.junit.jupiter.api.*;

import java.util.Random;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
offlinePlayer
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EconomyTest {

    static Database database;
    static EconomyService economyService;

    @BeforeAll
    static void beforeAll() {
        database = DB.getDefault();
        economyService = new EconomyService(database, new GlobalEventHandler());
        Assertions.assertNotNull(database);
    }

    @Test
    @Order(1)
    void createBankAccount() {
        BankAccount bankAccount = new BankAccount(Double.MAX_VALUE);
        database.insert(bankAccount);

        BankAccount found = database.find(BankAccount.class, 1);
        Assertions.assertNotNull(found);
    }

    @Test
    @Order(2)
    void createPlayerAccount() {
        PlayerAccount bankAccount = new PlayerAccount(0);
        database.insert(bankAccount);

        PlayerAccount found = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(found);
    }

    @Test
    @Order(3)
    void tryNegativeWithdraw() {
        PlayerAccount account = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(account);

        Assertions.assertThrows(Exception.class, () -> {
            economyService.withdraw(account, -1, "test", CCTransaction.Type.PLAYER).join();
        });
    }

    @Test
    @Order(4)
    void tryNegativeDeposit() {
        PlayerAccount account = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(account);

        Assertions.assertThrows(IllegalArgumentException.class, () -> economyService.deposit(account, -1, "test", CCTransaction.Type.PLAYER));
    }

    @Test
    @Order(5)
    void tryWithdrawWithoutBalance() {
        PlayerAccount account = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(account);
        Assertions.assertFalse(economyService.withdraw(account, Double.MAX_VALUE, "test", CCTransaction.Type.PLAYER).join());
    }

    @Test
    @Order(6)
    void tryDeposit() {
        PlayerAccount account = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(account);
        double amount = new Random().nextDouble();

        economyService.deposit(account, amount, "test", CCTransaction.Type.PLAYER);

        Assertions.assertEquals(amount, account.getBalance());
    }

    @Test
    @Order(7)
    void tryWithdraw() {
        PlayerAccount account = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(account);

        economyService.withdraw(account, account.getBalance(), "test", CCTransaction.Type.PLAYER);

        Assertions.assertEquals(0, account.getBalance());
    }

    @Test
    @Order(8)
    void tryTransfer() {
        BankAccount account = database.find(BankAccount.class, 1);
        Assertions.assertNotNull(account);
        PlayerAccount account2 = database.find(PlayerAccount.class, 1);
        Assertions.assertNotNull(account2);

        double amount = new Random().nextDouble();
        double initialBalance = account.getBalance();


        economyService.transfer(account, account2, amount, "test", CCTransaction.Type.PLAYER);

        Assertions.assertEquals(initialBalance - amount, account.getBalance());
        Assertions.assertEquals(amount, account2.getBalance());
    }

    @AfterAll
    static void afterAll() {
        database.shutdown(true, false);
        database = null;
    }
}
