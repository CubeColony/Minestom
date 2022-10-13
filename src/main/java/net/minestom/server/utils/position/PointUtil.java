package net.minestom.server.utils.position;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.ApiStatus;

/**
 * @author LBuke (Teddeh)
 */
@ApiStatus.NonExtendable
public final class PointUtil {

    /**
     * It takes a Pos object and returns a String.
     *
     * @param pos The position to convert to a string.
     * @return A string.
     */
    public static String asString(Pos pos) {
        return "%s, %s, %s".formatted(pos.x(), pos.y(), pos.z());
    }
}
