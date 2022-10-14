package net.minestom.server.cubecolony.redis;

import com.cubecolony.api.annotation.AwaitingDocumentation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author LBuke (Teddeh)
 */
@AwaitingDocumentation
public interface Redisable {

    Gson GSON = new Gson();

    default String toJson() {
        return GSON.toJson(this);
    }

    static <T> T fromJson(String json, Class<T> clazz) {
        final JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        if (jsonObject.has("content")) {
            json = jsonObject.get("content").getAsString();
        }
        return GSON.fromJson(json, clazz);
    }
}
