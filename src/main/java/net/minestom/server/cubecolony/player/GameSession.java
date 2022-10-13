package net.minestom.server.cubecolony.player;

import com.cubecolony.api.players.CCPlayer;
import com.cubecolony.api.players.CCSession;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.Locale;

/**
 * Cubestom
 *
 * @author Roch Blondiaux

 */
@Entity
@Table(name = "sessions")
public class GameSession implements CCSession {

    @Id
    private long id;
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
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

    @Override
    public long getId() {
        return this.id;
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

    @Override
    public String toString() {
        return "GameSession{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", locale=" + locale +
                ", usingLauncher=" + usingLauncher +
                ", version='" + version + '\'' +
                ", launcherVersion='" + launcherVersion + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
