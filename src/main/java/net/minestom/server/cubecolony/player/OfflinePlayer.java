package net.minestom.server.cubecolony.player;

import com.cubecolony.api.economy.game.CCBankAccount;
import com.cubecolony.api.economy.game.CCPlayerAccount;
import com.cubecolony.api.economy.store.CCPurchase;
import com.cubecolony.api.friends.CCFriendshipRequest;
import com.cubecolony.api.players.CCPlayer;
import com.cubecolony.api.players.CCPlayerPreferences;
import com.cubecolony.api.players.CCSession;
import com.cubecolony.api.punishments.CCPunishment;
import com.cubecolony.api.ranks.CCRank;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import net.minestom.server.cubecolony.economy.game.BankAccount;
import net.minestom.server.cubecolony.economy.game.PlayerAccount;
import net.minestom.server.cubecolony.economy.store.Purchase;
import net.minestom.server.cubecolony.friends.FriendshipRequest;
import net.minestom.server.cubecolony.punishments.Punishment;
import net.minestom.server.cubecolony.ranks.Rank;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.awt.*;
import java.util.*;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 */

@Entity
@Table(name = "players")
public class OfflinePlayer implements CCPlayer {

    @Id
    private long id;
    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column
    private Locale locale = Locale.ENGLISH;
    @Column(name = "play_time")
    private long playTime;
    @Column(name = "last_login")
    private Date lastLogin;
    @OneToOne
    private PlayerPreferences preferences;
    @OneToOne
    private BankAccount bankAccount;
    @OneToOne(targetEntity = PlayerAccount.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CCPlayerAccount account;
    @OneToOne(targetEntity = Rank.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CCRank rank;
    @OneToMany(targetEntity = Purchase.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CCPurchase> purchases = new HashSet<>();
    @OneToMany(targetEntity = Punishment.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CCPunishment> punishments = new HashSet<>();
    @OneToMany(targetEntity = GameSession.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "players_sessions", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "session_id"))
    private Set<CCSession> sessions = new HashSet<>();
    @ManyToMany(targetEntity = FriendshipRequest.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CCFriendshipRequest> friendshipRequests = new HashSet<>();
    @ManyToMany(targetEntity = OfflinePlayer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "friends",
            joinColumns = {@JoinColumn(name = "player_1_id"), @JoinColumn(name = "player_2_id")},
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<CCPlayer> friends = new HashSet<>();
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;


    public OfflinePlayer(UUID uuid, String name, Locale locale, long playTime, Date lastLogin, PlayerPreferences preferences, BankAccount bankAccount, PlayerAccount account, CCRank rank, Set<CCPurchase> purchases, Set<CCPunishment> punishments, Set<CCSession> sessions, Set<CCFriendshipRequest> friendshipRequests, Set<CCPlayer> friends) {
        this.uuid = uuid;
        this.name = name;
        this.locale = locale;
        this.playTime = playTime;
        this.lastLogin = lastLogin;
        this.preferences = preferences;
        this.bankAccount = bankAccount;
        this.account = account;
        this.rank = rank;
        this.purchases = purchases;
        this.punishments = punishments;
        this.sessions = sessions;
        this.friendshipRequests = friendshipRequests;
        this.friends = friends;
    }

    public OfflinePlayer() {
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    @Override
    public @NotNull Component getName() {
        return null; // FIXME: 12/10/2022
    }

    @Override
    public void setName(@NotNull String s) {
        this.name = s;
    }

    @Override
    public @NotNull Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(@NotNull Locale locale) {
        this.locale = locale;
    }

    @Override
    public long getPlayTime() {
        return playTime;
    }

    @Override
    public void setPlayTime(long l) {
        this.playTime = l;
    }

    @Override
    public @NotNull Date getLastLogin() {
        return lastLogin;
    }

    @Override
    public void setLastLogin(@NotNull Date date) {
        this.lastLogin = date;
    }

    @Override
    public @NotNull CCPlayerPreferences getPreferences() {
        return preferences;
    }

    @Override
    public @NotNull CCBankAccount getBankAccount() {
        return bankAccount;
    }

    @Override
    public @NotNull CCPlayerAccount getAccount() {
        return account;
    }

    @Override
    public @NotNull CCRank getRank() {
        return rank;
    }

    @Override
    public void setRank(@NotNull CCRank ccRank) {
        this.rank = ccRank;
    }

    @Override
    public @NotNull Set<CCPurchase> getPurchases() {
        return purchases;
    }

    @Override
    public @NotNull Set<CCPunishment> getPunishments() {
        return punishments;
    }

    @Override
    public @NotNull Set<CCSession> getSessions() {
        return sessions;
    }

    @Override
    public @NotNull Set<CCFriendshipRequest> getFriendshipRequests() {
        return friendshipRequests;
    }

    @Override
    public @NotNull Set<CCPlayer> getFriends() {
        return friends;
    }

    @Override
    public boolean areFriends(@NotNull CCPlayer ccPlayer) {
        return false;
    }

    @Override
    public @NotNull Date getLastUpdateDate() {
        return this.updatedAt;
    }

    @Override
    public @NotNull Date getCreationDate() {
        return this.createdAt;
    }

    @Override
    public String toString() {
        return "OfflinePlayer{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", locale=" + locale +
                ", playTime=" + playTime +
                ", lastLogin=" + lastLogin +
                ", preferences=" + preferences +
                ", bankAccount=" + bankAccount +
                ", account=" + account +
                ", rank=" + rank +
                ", purchases=" + purchases +
                ", punishments=" + punishments +
                ", sessions=" + sessions +
                ", friendshipRequests=" + friendshipRequests +
                ", friends=" + friends +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
