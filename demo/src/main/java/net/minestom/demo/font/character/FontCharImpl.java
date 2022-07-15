package net.minestom.demo.font.character;

import net.minestom.demo.font.key.FontKey;
import org.jetbrains.annotations.NotNull;

/**
 * @author LBuke (Teddeh)
 */
public final class FontCharImpl implements FontChar {
    private final char character;
    private final byte length;
    private final FontKey key;

    public FontCharImpl(char character, int length, @NotNull FontKey key) {
        this.character = character;
        this.length = (byte) length;
        this.key = key;

        REGISTRY.put(character, this);
    }

    @Override
    public char getChar() {
        return this.character;
    }

    @Override
    public byte getLength() {
        return this.length;
    }

    @Override
    public @NotNull FontKey getFontKey() {
        return this.key;
    }
}
