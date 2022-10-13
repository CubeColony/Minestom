package net.minestom.server.cubecolony.i18n;

import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.moandjiezana.toml.Toml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * @author LBuke (Teddeh)
 */
@SuppressWarnings({"unused"})
public class I18n {

    public final static Logger LOGGER = LoggerFactory.getLogger(I18n.class);

    private static final MiniMessage MARKDOWN = MiniMessage.miniMessage();

    private CommandSender sender;
    private LocaleConstants localeConstants;
    private Object[] parameters;
    private String message;

    static {
        Toml.init(new GsonBuilder().excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT).create());
        Objects.requireNonNull(loadTomlFile("lang/global.toml")).to(Constants.class);

        for (Type type : Type.values()) {
            final String code = type.getLocale().toString();

            final Toml toml = loadTomlFile("lang/%s.toml".formatted(code));
            if (Objects.isNull(toml)) continue;

            final LocaleConstants constants = toml.to(LocaleConstants.class);
            type.setConstants(constants);

            LOGGER.info("Registered Language: {} {}", code, type.getLocale().getDisplayName());
        }
    }

    /**
     * It takes a string and returns a component
     *
     * @param input The markdown to parse.
     * @return A component.
     */
    public static @NotNull Component markdown(@NotNull String input) {
        return MARKDOWN.deserialize(input);
    }

    /**
     * The function creates an I18n object and sets its sender and parameters fields.
     * It also sets the localeConstants field to the constants of the locale of the sender
     *
     * @param sender The sender of the command.
     * @return An I18n object.
     */
    public static @NotNull I18n of(@Nullable CommandSender sender, @NotNull Object... parameters) {
        final I18n i18n = new I18n();
        i18n.sender = sender;
        i18n.parameters = parameters;

        Type type = Type.UNITED_STATES;
        if (sender instanceof Player player) {
            final Locale locale = player.getLocale();
            if (Objects.nonNull(locale))
                type = Type.of(locale);
        }

        i18n.localeConstants = type.constants;
        return i18n;
    }

    /**
     * The function creates an I18n object and sets its sender and parameters fields.
     * It also sets the localeConstants field to the constants of the locale of the sender
     *
     * @return An I18n object.
     */
    public static @NotNull I18n of(@NotNull Object... parameters) {
        return of(null, parameters);
    }

    /**
     * It takes a ConsumeLocale object and sends a message to the user
     *
     * @param consumer The consumer that is being used to consume the message.
     */
    public void message(@NotNull LocaleConsumer consumer) {
        final String message = this.prepareParams(consumer);
        this.sender.sendMessage(MARKDOWN.deserialize(message));
    }

    /**
     * It takes a consumer of type `ConsumeLocale` and returns a `Component`
     *
     * @param consumer The consumer is the object that will consume the message.
     * @return The Markdown component.
     */
    public Component asComponent(@NotNull LocaleConsumer consumer) {
        final String message = this.prepareParams(consumer);
        return MARKDOWN.deserialize(message);
    }

    /**
     * It takes a consumer that will consume the parameters of the message,
     * and returns a string that is the message with the parameters replaced
     *
     * @param consumer The consumer is a function that takes a string and returns a string.
     * @return The parsed message.
     */
    public @NotNull String asString(@NotNull LocaleConsumer consumer) {
        final String message = this.prepareParams(consumer);
        final TextComponent parse = (TextComponent) MARKDOWN.deserialize(message);
        return parse.content();
    }

    /**
     * It takes a consumer of type `ConsumeLocale` and returns a string
     *
     * @param consumer The consumer to use to consume the locale.
     * @return The message with the parameters replaced.
     */
    private @NotNull String prepareParams(@NotNull LocaleConsumer consumer) {
        String message = consumer.accept(this.localeConstants);
        for (int i = 0; i < this.parameters.length; i++) {
            message = message.replaceAll("\\{%s}".formatted(i), String.valueOf(this.parameters[i]));
        }
        return message;
    }

    private static @Nullable Toml loadTomlFile(@NotNull String file) {
        try (final InputStream inputStream = I18n.class.getClassLoader().getResourceAsStream(file)) {
            if (Objects.isNull(inputStream))
                return null;
            try {
                return new Toml().read(inputStream);
            } catch (ExceptionInInitializerError e) {
                e.printStackTrace();
            }
        } catch (IOException throwable) {
            throw new RuntimeException(throwable);
        }
        return null;
    }

    public enum Type {
        UNITED_KINGDOM((byte) 1, Locale.UK),
        UNITED_STATES((byte) 2, Locale.US),
        CANADA((byte) 3, Locale.CANADA),
        CANADA_FRENCH((byte) 4, Locale.CANADA_FRENCH),
        FRANCE((byte) 5, Locale.FRANCE),
        GERMAN((byte) 6, Locale.GERMANY),
        ITALY((byte) 7, Locale.ITALY),
        JAPAN((byte) 8, Locale.JAPAN),
        KOREA((byte) 9, Locale.KOREA),
        SIMPLIFIED_CHINESE((byte) 10, Locale.SIMPLIFIED_CHINESE),
        TRADITIONAL_CHINESE((byte) 11, Locale.TRADITIONAL_CHINESE);

        private final byte id;
        private final Locale locale;
        private LocaleConstants constants;

        Type(byte id, @NotNull Locale locale) {
            this.id = id;
            this.locale = locale;
        }

        public byte getId() {
            return this.id;
        }

        public @NotNull Locale getLocale() {
            return this.locale;
        }

        public void setConstants(@NotNull LocaleConstants constants) {
            this.constants = constants;
        }

        public static @NotNull Type of(@NotNull Locale locale) {
            return Arrays.stream(values())
                    .filter(type -> type.getLocale().equals(locale))
                    .findFirst()
                    .orElse(UNITED_STATES);
        }
    }
}
