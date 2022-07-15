package net.minestom.demo.font.key;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

/**
 * @author LBuke (Teddeh)
 */
public sealed interface FontKey extends FontKeys permits FontKeyImpl {

    @NotNull String getNamespace();

    @NotNull String getFontName();

    @NotNull Key getKey();
}
