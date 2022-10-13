package net.minestom.server.cubecolony.player;

import com.cubecolony.api.players.CCSession;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column(name = "ended_at")
    private Date endDate;
    @Column(name = "created_at")
    @WhenCreated
    protected Date createdAt;
    @Column(name = "updated_at")
    @WhenModified
    protected Date updatedAt;

    public GameSession(String ip, Locale locale, boolean usingLauncher, String version, String launcherVersion) {
        this.ip = ip;
        this.locale = locale;
        this.usingLauncher = usingLauncher;
        this.version = version;
        this.launcherVersion = launcherVersion;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public GameSession() {
    }

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
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
                ", endDate=" + endDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
