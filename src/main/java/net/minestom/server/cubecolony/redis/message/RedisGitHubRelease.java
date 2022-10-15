package net.minestom.server.cubecolony.redis.message;

import net.minestom.server.cubecolony.redis.Redisable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author LBuke (Teddeh)
 */
public final class RedisGitHubRelease implements Redisable, Serializable {

    @Serial
    private static final long serialVersionUID = 0L;

    private final String version;
    private final String description;

    public RedisGitHubRelease(String version, String description) {
        this.version = version;
        this.description = description;
    }

    /**
     * Returns the version of the release.
     *
     * @return The version of the release.
     */
    public String version() {
        return this.version;
    }

    /**
     * Returns the description of the release.
     *
     * @return The description of the release.
     */
    public String description() {
        return this.description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final var that = (RedisGitHubRelease) obj;
        return Objects.equals(this.version, that.version) &&
                Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.version, this.description);
    }

    @Override
    public String toString() {
        return "RedisGitHubRelease[" +
                "version=" + this.version + ", " +
                "description=" + this.description + ']';
    }

}