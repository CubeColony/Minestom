package net.minestom.demo.font.key;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

/**
 * @author LBuke (Teddeh)
 */
public final class FontKeyImpl implements FontKey {
    private final String namespace;
    private final String fontName;
    private final Key key;

    @SuppressWarnings("PatternValidation")
    public FontKeyImpl(@NotNull String namespace, @NotNull String fontName) {
        this.namespace = namespace;
        this.fontName = fontName;
        this.key = Key.key(namespace, fontName);
    }

    @Override
    public @NotNull String getNamespace() {
        return this.namespace;
    }

    @Override
    public @NotNull String getFontName() {
        return this.fontName;
    }

    @Override
    public @NotNull Key getKey() {
        return this.key;
    }
}
