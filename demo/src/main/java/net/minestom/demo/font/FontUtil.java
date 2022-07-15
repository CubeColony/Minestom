package net.minestom.demo.font;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.minestom.demo.font.key.FontKey;
import org.jetbrains.annotations.NotNull;

/**
 * @author LBuke (Teddeh)
 */
public final class FontUtil {
    private static final Pair<Integer, Character>[] SPACES;
    private static final Pair<Integer, Character>[] NEG_SPACES;

    public static @NotNull String append(int pixels) {
        return parse(pixels, SPACES);
    }

    public static @NotNull Component appendC(int pixels) {
        return Component.text(parse(pixels, SPACES)).font(FontKey.SPECIAL.getKey());
    }

    public static @NotNull String prepend(int pixels) {
        return parse(pixels, NEG_SPACES);
    }

    public static @NotNull Component prependC(int pixels) {
        return Component.text(parse(pixels, NEG_SPACES)).font(FontKey.SPECIAL.getKey());
    }

    private static @NotNull String parse(int pixels, @NotNull Pair<Integer, Character>[] pairArray) {
        final StringBuilder builder = new StringBuilder();
        for (Pair<Integer, Character> pair : pairArray) {
            final int mask = pair.left();
            if (pixels >= mask) {
                pixels -= mask;
                builder.append(pair.right()).append(' ');
                if (pixels == 0)
                    break;
            }
        }
        return builder.toString();
    }

    static {
        NEG_SPACES = new Pair[11];
        NEG_SPACES[10] = Pair.of(1, '\uE000');
        NEG_SPACES[9] = Pair.of(2, '\uE001');
        NEG_SPACES[8] = Pair.of(4, '\uE002');
        NEG_SPACES[7] = Pair.of(8, '\uE003');
        NEG_SPACES[6] = Pair.of(16, '\uE004');
        NEG_SPACES[5] = Pair.of(32, '\uE005');
        NEG_SPACES[4] = Pair.of(64, '\uE006');
        NEG_SPACES[3] = Pair.of(128, '\uE007');
        NEG_SPACES[2] = Pair.of(256, '\uE008');
        NEG_SPACES[1] = Pair.of(512, '\uE009');
        NEG_SPACES[0] = Pair.of(1024, '\uE00A');

        SPACES = new Pair[11];
        SPACES[10] = Pair.of(1, '\uE010');
        SPACES[9] = Pair.of(2, '\uE011');
        SPACES[8] = Pair.of(4, '\uE012');
        SPACES[7] = Pair.of(8, '\uE013');
        SPACES[6] = Pair.of(16, '\uE014');
        SPACES[5] = Pair.of(32, '\uE015');
        SPACES[4] = Pair.of(64, '\uE016');
        SPACES[3] = Pair.of(128, '\uE017');
        SPACES[2] = Pair.of(256, '\uE018');
        SPACES[1] = Pair.of(512, '\uE019');
        SPACES[0] = Pair.of(1024, '\uE01A');
    }

    public static void main(String[] args) {
        System.out.println("100 -> " + append(100));
        System.out.println("100 -> " + append(100));
        System.out.println("100 -> " + append(100));
        System.out.println("100 -> " + append(100));
        System.out.println("100 -> " + prepend(100));
        System.out.println("100 -> " + prepend(100));
        System.out.println("193 -> " + prepend(193));
        System.out.println("193 -> " + prepend(193));
    }
}
