package net.minestom.server.cubecolony.region;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.awt.*;

/**
 * @author LBuke (Teddeh)
 */
public enum RegionType {
    WALKWAY((byte) 1, new Color(0, 200, 0, 60)),
    ROAD((byte) 2, new Color(200, 200, 200, 60)),
    ;

    private final byte id;
    private final Color color;

    RegionType(byte id, @NotNull Color color) {
        this.id = id;
        this.color = color;
    }

    public byte getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public static @UnknownNullability RegionType of(byte id) {
        for (RegionType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
