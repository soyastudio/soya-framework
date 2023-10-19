package soya.framework.commons.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JsonObject extends JSON {

    private final Map<String, JSON> members;

    protected JsonObject() {
        this.members = new LinkedHashMap<>();
    }

    public void add(String propName, JSON propValue) {
        members.put(propName, propValue == null ? JSON_NULL : propValue);
    }

    public JSON remove(String property) {
        return members.remove(property);
    }

    public void addProperty(String property, String value) {
        add(property, value == null ? JSON_NULL : new JsonPrimitive(value));
    }

    public void addProperty(String property, Number value) {
        add(property, value == null ? JSON_NULL : new JsonPrimitive(value));
    }

    public void addProperty(String property, Boolean value) {
        add(property, value == null ? JSON_NULL : new JsonPrimitive(value));
    }

    public void addProperty(String property, double value) {
        add(property, new JsonPrimitive(value));
    }

    public void addProperty(String property, float value) {
        add(property, new JsonPrimitive(value));
    }

    public void addProperty(String property, long value) {
        add(property, new JsonPrimitive(value));
    }

    public void addProperty(String property, int value) {
        add(property, new JsonPrimitive(value));
    }

    public void addProperty(String property, short value) {
        add(property, new JsonPrimitive(value));
    }

    public Set<Map.Entry<String, JSON>> entrySet() {
        return members.entrySet();
    }
}
