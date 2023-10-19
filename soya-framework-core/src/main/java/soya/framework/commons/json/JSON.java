package soya.framework.commons.json;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public abstract class JSON implements Serializable {
    public static final JsonNull JSON_NULL = new JsonNull();

    protected JSON() {
    }

    public boolean isJsonNull() {
        return this instanceof JsonNull;
    }

    public boolean isJsonPrimitive() {
        return this instanceof JsonPrimitive;
    }

    public boolean isBoolean() {
        return isJsonPrimitive() && ((JsonPrimitive) this).isBoolean();
    }

    public boolean isNumber() {
        return isJsonPrimitive() && ((JsonPrimitive) this).isNumber();
    }

    public boolean isString() {
        return isJsonPrimitive() && ((JsonPrimitive) this).isString();
    }

    public boolean isJsonObject() {
        return this instanceof JsonObject;
    }

    public boolean isJsonArray() {
        return this instanceof JsonArray;
    }

    public JsonNull asJsonNull() {
        if (this instanceof JsonNull) {
            return (JsonNull) this;
        }
        throw new JSONException("Cannot cast to JsonNull from " + this.getClass().getSimpleName());
    }

    public JsonPrimitive asJsonPrimitive() {
        if (this instanceof JsonPrimitive) {
            return (JsonPrimitive) this;
        }
        throw new JSONException("Cannot cast to JsonPrimitive from " + this.getClass().getSimpleName());
    }

    public JsonObject asJsonObject() {
        if (this instanceof JsonObject) {
            return (JsonObject) this;
        }
        throw new JSONException("Cannot cast to JsonObject from " + this.getClass().getSimpleName());
    }

    public JsonArray asJsonArray() {
        if (this instanceof JsonArray) {
            return (JsonArray) this;
        }
        throw new JSONException("Cannot cast to JsonArray from " + this.getClass().getSimpleName());
    }

    public String toString() {
        JsonStringBuilder builder = new JsonStringBuilder(-1);
        return builder.toString();
    }

    public String toPrettyString() {
        JsonStringBuilder builder = new JsonStringBuilder(0);
        return builder.toString();
    }

    private void render(JSON data, StringBuilder builder, int indent) {
        if (data.isJsonNull()) {
            builder.append("null");
        } else if (data.isBoolean()) {
            builder.append(data.asJsonPrimitive().booleanValue());
        } else if (data.isNumber()) {
            builder.append(data.asJsonPrimitive().stringValue());
        } else if (data.isString()) {
            builder.append("\"").append(data.asJsonPrimitive().shortValue()).append("\"");
        } else if (data.isJsonArray()) {

        } else if (data.isJsonObject()) {

        }
    }

    public static JsonPrimitive newJsonPrimitive(String value) {
        return new JsonPrimitive(value);
    }

    public static JsonPrimitive newJsonPrimitive(Boolean value) {
        return new JsonPrimitive(value);
    }

    public static JsonPrimitive newJsonPrimitive(Number value) {
        return new JsonPrimitive(value);
    }

    public static JsonArray newJsonArray() {
        return new JsonArray();
    }

    public static JsonObject newJsonObject() {
        return new JsonObject();
    }

    public static JSON parse(String st) {
        return null;
    }

    public static JSON parse(Reader reader) {
        return null;
    }

    public static JSON parse(InputStream stream) {
        return null;
    }

    public static JSON toJson(Object object) {
        if (object == null) {
            return JSON_NULL;

        } else if (object instanceof Boolean) {
            return newJsonPrimitive((Boolean) object);

        } else if (object instanceof Number) {
            return newJsonPrimitive((Number) object);

        } else if (object instanceof String) {
            return newJsonPrimitive((String) object);

        } else if (object.getClass().isArray()) {
            JsonArray jsonArray = newJsonArray();
            int len = Array.getLength(object);
            for (int i = 0; i < len; i++) {
                jsonArray.add(toJson(Array.get(object, i)));
            }
            return jsonArray;

        } else if (object instanceof Collection) {
            JsonArray jsonArray = newJsonArray();
            ((Collection) object).forEach(e -> {
                jsonArray.add(toJson(e));
            });
            return jsonArray;

        } else if (object instanceof Map) {
            JsonObject jsonObject = newJsonObject();
            Map<String, Object> map = (Map<String, Object>) object;
            map.entrySet().forEach(e -> {
                jsonObject.add(e.getKey(), toJson(e.getValue()));
            });

            return jsonObject;

        }

        return null;
    }

    private static class JsonStringBuilder {
        private static String[] indents = new String[]{
                "",
                "\t",
                "\t\t",
                "\t\t\t",
                "\t\t\t\t",
                "\t\t\t\t\t",
                "\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
        };

        private final StringBuilder builder;

        private int indentLevel;

        private JsonStringBuilder(int indentLevel) {
            this.builder = new StringBuilder();
            this.indentLevel = indentLevel;
        }

        private static JsonStringBuilder builder() {
            return new JsonStringBuilder(0);
        }

        public JsonStringBuilder indentPush() {
            if (indentLevel >= 0) {
                indentLevel++;
            }
            return this;
        }

        public JsonStringBuilder indentPop() {
            if (indentLevel > 0) {
                indentLevel--;
            }

            return this;
        }

        public JsonStringBuilder appendLine() {
            builder.append("\n");
            return this;
        }

        public JsonStringBuilder append(JsonPrimitive primitive) {
            if (primitive.isString()) {
                builder.append("\"").append(primitive.stringValue()).append("\"");
            } else {
                builder.append(primitive.stringValue());
            }
            return this;
        }

        public JsonStringBuilder append(JsonObject object) {
            if (indentLevel < 0) {
                builder.append("{");
            } else {
                builder.append(indents[indentLevel]).append("{\n");
                indentLevel ++;
            }

            object.entrySet().forEach(e -> {
                append(e);
            });

            if (indentLevel > 0) {
                indentLevel --;
                builder.append(indents[indentLevel]).append("}");
            } else {
                builder.append("}");
            }
            return this;
        }

        private JsonStringBuilder append(Map.Entry<String, JSON> entry) {
            if (indentLevel < 0) {
                builder.append("\"").append(entry.getKey()).append("\":");

            } else {
                builder.append("\"").append(entry.getKey()).append("\": ");
            }

            JSON json = entry.getValue();
            if(json.isJsonPrimitive()) {
                append(json.asJsonPrimitive());
            } else if(json.isJsonObject()) {
                JsonObject jsonObject = json.asJsonObject();

            }


            return this;
        }

        public String toString() {
            return builder.toString();
        }
    }

    public static void main(String[] arg) {
        JsonStringBuilder builder = JsonStringBuilder.builder();
        JsonPrimitive primitive = JSON.newJsonPrimitive("true");
        System.out.println(builder.append(primitive).toString());
    }
}
