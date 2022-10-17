package net.minestom.server.cubecolony.region;

import com.cubecolony.api.annotation.Documented;
import net.minestom.server.coordinate.Point;
import org.jetbrains.annotations.NotNull;

/**
 * @author LBuke (Teddeh)
 */
@Documented
public record BoundingBox(int maxX, int maxY, int maxZ,
                          int minX, int minY, int minZ) {

    /**
     * Return the midpoint of the x-axis
     *
     * @return The midpoint of the x-axis.
     */
    public double getMidX() {
        final int max = this.maxX();
        final int min = this.minX();
        final int length = max - min;
        return min + (length / 2D);
    }

    /**
     * Return the midpoint of the y-axis
     *
     * @return The midpoint of the y-axis.
     */
    public double getMidY() {
        final int max = this.maxY();
        final int min = this.minY();
        final int length = max - min;
        return min + (length / 2D);
    }

    /**
     * Return the midpoint of the z-axis
     *
     * @return The midpoint of the z-axis.
     */
    public double getMidZ() {
        final int max = this.maxZ();
        final int min = this.minZ();
        final int length = max - min;
        return min + (length / 2D);
    }

    public boolean isInside(@NotNull Point point) {
        return (point.x() <= this.maxX() && point.x() >= this.minX())
                && (point.y() <= this.maxY() && point.y() >= this.minY())
                && (point.z() <= this.maxZ() && point.z() >= this.minZ());
    }
}
