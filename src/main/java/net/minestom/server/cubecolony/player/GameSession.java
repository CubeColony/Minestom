package net.minestom.server.cubecolony.player;

import com.cubecolony.api.players.CCPlayer;
import com.cubecolony.api.players.CCSession;
import net.minestom.server.cubecolony.JPAModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Locale;

/**
 * Cubestom
 *
 * @author Roch Blondiaux
 * @date 12/10/2022
 */
@Entity
@Table(name = "sessions")
public class GameSession extends JPAModel implements CCSession {

    @OneToOne
    private OfflinePlayer player;
    @Column
    private String ip;
    @Column
    private Locale locale;
    @Column(name = "launcher")
    private boolean usingLauncher;
    @Column
    private String version;
    @Column(name = "launcher_version")
    private String launcherVersion;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull CCPlayer getPlayer() {
        return player;
    }

    @Override
    public @NotNull String getIp() {
        return ip;
    }

    @Override
    public boolean isUsingLauncher() {
        return usingLauncher;
    }

    @Override
    public @Nullable String getLauncherVersion() {
        return launcherVersion;
    }

    @Override
    public @NotNull Locale getLocale() {
        return locale;
    }

    @Override
    public @NotNull String getVersion() {
        return version;
    }

    @Override
    public @NotNull Date getStartDate() {
        return this.createdAt;
    }

    @Override
    public @Nullable Date getEndDate() {
        return this.updatedAt;
    }
}
