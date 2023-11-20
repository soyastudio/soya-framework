package soya.framework.commons.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

public class JsonUtils {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private JsonUtils() {
    }

    public static String toJson(Object o) {
        if (o.getClass().isArray() || o instanceof Collection) {
            return GSON.toJson(toJsonArray(toArray(o)));

        } else if (o instanceof DynaBean) {
            return GSON.toJson(BeanUtils.toMap((DynaBean<?>) o));

        } else {
            return GSON.toJson(o);
        }
    }

    private static JsonArray toJsonArray(Object[] array) {
        JsonArray jsonArray = new JsonArray();
        Arrays.stream(array).forEach(e -> {
            if (e instanceof DynaBean) {
                jsonArray.add(GSON.toJsonTree(BeanUtils.toMap((DynaBean<?>) e)));

            } else if (e.getClass().isArray() || e instanceof Collection) {
                jsonArray.add(toJsonArray(toArray(e)));

            } else {
                jsonArray.add(GSON.toJsonTree(e));
            }
        });

        return jsonArray;
    }

    private static Object[] toArray(Object o) {
        if (o instanceof Collection) {
            Collection collection = (Collection) o;
            return collection.toArray(new Object[collection.size()]);

        } else if (o.getClass().isArray()) {
            Object[] array = new Object[Array.getLength(o)];
            for (int i = 0; i < array.length; i++) {
                array[i] = Array.get(o, i);
            }
            return array;

        } else {
            throw new IllegalArgumentException("Cannot convert to array from: " + o.getClass().getName());
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static DynaBean fromJson(String json, DynaClass dynaClass) {

        try {
            DynaBean bean = dynaClass.newInstance();
            // TODO:
            return bean;
        } catch (IllegalAccessException | InstantiationException e) {

            throw new RuntimeException(e);
        }
    }
}
