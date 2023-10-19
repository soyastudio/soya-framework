package soya.framework.commons.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray extends JSON implements Iterable<JSON> {
    private final ArrayList<JSON> elements;

    protected JsonArray() {
        this.elements = new ArrayList<>();
    }

    @Override
    public Iterator<JSON> iterator() {
        return elements.iterator();
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public List<JSON> asList() {
        return new ArrayList<>(elements);
    }

    public JSON get(int index) {
        return elements.get(index);
    }

    public void add(JSON element) {
        if (element == null) {
            elements.add(JSON.JSON_NULL);
        } else {
            elements.add(element);
        }
    }

    public void add(Boolean element) {
        if (element == null) {
            elements.add(JSON.JSON_NULL);
        } else {
            elements.add(newJsonPrimitive(element));
        }
    }

    public void add(Number element) {
        if (element == null) {
            elements.add(JSON.JSON_NULL);
        } else {
            elements.add(newJsonPrimitive(element));
        }
    }

    public void add(String element) {
        if (element == null) {
            elements.add(JSON.JSON_NULL);
        } else {
            elements.add(newJsonPrimitive(element));
        }
    }

    public void addAll(JsonArray array) {
        if (array != null) {
            this.elements.addAll(array.elements);
        }
    }

    public JsonPrimitive getAsJsonPrimitive(int index) {
        return elements.get(index).asJsonPrimitive();
    }

    public JsonObject getAsJsonObject(int index) {
        return elements.get(index).asJsonObject();
    }

    public JsonArray getAsJsonArray(int index) {
        return elements.get(index).asJsonArray();
    }


}
