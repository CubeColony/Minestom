package net.minestom.demo.test;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;

/**
 * @author LBuke (Teddeh)
 */
public final class BlockTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockTest.class);
    private static final Gson GSON = new Gson();

    public static void main(String[] args) {
        final BlockTest test = new BlockTest();

        try (Reader reader = new InputStreamReader(Objects.requireNonNull(test.getClass().getResourceAsStream("/block_loot_tables.json")))) {
            final JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            for (Block block : Block.values()) {
                final NamespaceID namespace = block.registry().namespace();
                final JsonObject testObject = jsonObject.getAsJsonObject(namespace.asString());
                if (Objects.isNull(testObject))
                    continue;

                LOGGER.info("{} --> {}", block.name(), testObject);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
