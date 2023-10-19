package soya.framework.commons.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonUtils {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtils() {
    }

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }
}
