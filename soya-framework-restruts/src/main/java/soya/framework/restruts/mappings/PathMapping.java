package soya.framework.restruts.mappings;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class PathMapping {
    private String[] items;
    private Set<String> params = new LinkedHashSet<>();

    public PathMapping(String path) {
        String token = path.startsWith("/")? path.substring(1) : path;
        items = token.split("/");
    }

    public void add(String path) {
        PathMapping mapping = new PathMapping(path);
        String[] result = Arrays.copyOf(mapping.items, mapping.items.length + items.length);
        System.arraycopy(items, 0, result, mapping.items.length, items.length);
        this.items = result;
    }

    public String[] getItems() {
        return items;
    }

    public boolean match(String path) {
        if(!path.startsWith("/")) {
            return false;
        }

        PathMapping mapping = new PathMapping(path);
        if(items.length != mapping.items.length) {
            return false;
        }

        for(int i = 0; i < items.length; i ++) {
            String item = items[i];
            if(!item.equals(mapping.items[i]) && !item.contains("{") && !item.contains("}")) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String item: items) {
            builder.append("/").append(item);
        }
        return builder.toString();
    }
}
