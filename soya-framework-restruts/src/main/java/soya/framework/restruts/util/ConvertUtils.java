package soya.framework.restruts.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.URI;

public class ConvertUtils {
    private static final Gson GSON = new Gson();

    protected ConvertUtils() {
    }

    public static Object convert(Object value, Class<?> type) {
        if (value == null) {
            return null;

        } else if (type.isInstance(value)) {
            return value;

        } else if ("java.lang.String".equals(type.getName())) {
            return value.toString();

        } else if ("java.net.URI".equals(type.getName())) {
            return URI.create(value.toString());

        } else if (value instanceof String) {
            String str = (String) value;
            if (Boolean.class == type || Boolean.TYPE == type) {
                return Boolean.parseBoolean(str);

            } else if (Byte.class == type || Byte.TYPE == type) {
                return Byte.parseByte(str);

            } else if (Short.class == type || Short.TYPE == type) {
                return Short.parseShort(str);

            } else if (Integer.class == type || Integer.TYPE == type) {
                return Integer.parseInt(str);

            } else if (Long.class == type || Long.TYPE == type) {
                return Long.parseLong(str);

            } else if (Float.class == type || Float.TYPE == type) {
                return Float.parseFloat(str);

            } else if (Double.class == type || Double.TYPE == type) {
                return Double.parseDouble(str);

            } else {
                return GSON.fromJson(JsonParser.parseString(str), type);
            }

        } else if (value instanceof JsonElement) {
            JsonElement jsonElement = (JsonElement) value;
            return GSON.fromJson(jsonElement, type);

        } else {
            try {
                return GSON.fromJson(GSON.toJsonTree(value), type);

            } catch (Exception e) {
                throw new ClassCastException("Cannot cast value of type'"
                        + value.getClass().getName()
                        + "' to type '" + type.getName() + "'.");
            }
        }
    }
}
