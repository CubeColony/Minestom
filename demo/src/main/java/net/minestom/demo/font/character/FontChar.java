package net.minestom.demo.font.character;

import net.minestom.demo.font.key.FontKey;
import org.jetbrains.annotations.NotNull;

/**
 * @author LBuke (Teddeh)
 */
public sealed interface FontChar extends FontChars permits FontCharImpl {

    char getChar();

    byte getLength();

    @NotNull FontKey getFontKey();

    static @NotNull FontChar fromChar(char character) {
        return REGISTRY.get(character);
    }
}
