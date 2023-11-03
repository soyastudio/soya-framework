package soya.framework.restruts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ActionMapping implements Comparable<ActionMapping>, Serializable {
    private String actionClass;
    private String action;
    private Path path;
    private String id;
    private String[] consumes;
    private String[] produces;
    private Map<String, ParameterMapping> parameters = new LinkedHashMap<>();
    private String[] tags;

    private ActionMapping(Class<?> actionClass, String action, HttpMethod method, String path) {
        this.actionClass = actionClass.getName();
        this.action = action;
        this.path = new Path(method.name(), path);
    }

    public Class<?> getActionClass() {
        try {
            return Class.forName(actionClass);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAction() {
        return action;
    }

    public Path getPath() {
        return path;
    }

    public boolean isPathMapping() {
        return path.pathItems != null;
    }

    public String getId() {
        return id;
    }

    public String[] getConsumes() {
        return consumes;
    }

    public String[] getProduces() {
        return produces;
    }

    public ParameterMapping[] getParameters() {
        return parameters.values().toArray(new ParameterMapping[parameters.size()]);
    }

    public ParameterMapping getParamMapping(String name) {
        return parameters.get(name);
    }

    public String[] getTags() {
        return tags;
    }

    public static Path path(String method, String path) {
        return new Path(method.toUpperCase(), path);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(ActionMapping o) {
        return path.compareTo(o.path);
    }

    public static class Builder {
        private Class<?> actionClass;

        private String action;
        private HttpMethod method;
        private String path;
        private String id;
        private String[] consumes;
        private String[] produces;
        private String[] tags = {};

        private Map<String, ParameterMapping> parameters = new LinkedHashMap<>();


        public HttpMethod getMethod() {
            return method;
        }

        private Builder() {
        }

        public Builder actionClass(Class<?> cls) {
            this.actionClass = cls;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder consumes(String[] consumes) {
            this.consumes = consumes;
            return this;
        }

        public Builder produces(String[] produces) {
            this.produces = produces;
            return this;
        }

        public Builder addParameter(String name, ParamType paramType, String referredTo, String desc) {
            parameters.put(name, new ParameterMapping(name, paramType, referredTo, desc));
            return this;
        }

        public Builder tags(String[] tags) {
            this.tags = tags;
            return this;
        }

        public ActionMapping create() {
            ActionMapping mapping = new ActionMapping(actionClass, action, method, path);

            mapping.id = id;
            mapping.consumes = consumes;
            mapping.produces = produces;
            mapping.parameters.putAll(parameters);
            mapping.tags = tags;

            return mapping;
        }
    }

    public static class Path implements Comparable<Path> {
        private final String method;
        private final String path;

        private String[] pathItems;

        Path(String method, String path) {
            this.method = method.toLowerCase();
            this.path = path;

            if (path.contains("{")) {
                pathItems = path.split("/");
            }
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Path that = (Path) o;
            if (!method.equalsIgnoreCase(that.method)) {
                return false;

            } else if (pathItems == null) {
                return path.equals(that.path);

            } else {
                return match(that.path);

            }
        }

        @Override
        public int hashCode() {
            int result = method != null ? method.hashCode() : 0;
            result = 31 * result + (path != null ? path.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(Path o) {
            int result = path.compareTo(o.path);
            if (result == 0) {
                result = sort(method) - sort(o.method);
            }

            return result;
        }

        public boolean match(String path) {
            String[] items = path.split("/");
            if(pathItems.length > items.length) {
                return false;

            } else {
                for(int i = 0; i < pathItems.length; i ++) {
                    if(!pathItems[i].equals(items[i]) && !pathItems[i].contains("{")) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Map<String, String> compile(String path) {
            Map<String, String> values = new HashMap<>();
            String[] items = path.split("/");
            for(int i = 0; i < pathItems.length; i ++) {
                String token = pathItems[i];
                if(token.startsWith("{") && token.endsWith("}")) {
                    String key = token.substring(1, token.length() - 1);
                    values.put(key, items[i]);
                }
            }
            return values;
        }

        private static int sort(String method) {
            switch (method) {
                case "get":
                    return 1;
                case "post":
                    return 2;
                case "put":
                    return 3;
                case "delete":
                    return 4;
                case "patch":
                    return 5;
                case "head":
                    return 6;
                case "option":
                    return 7;
                default:
                    return 100;
            }
        }

    }

    public static class ParameterMapping implements Serializable {
        private final String name;
        private final ParamType actionParameterType;

        private final String referredTo;

        private final String description;

        public ParameterMapping(String name, ParamType actionParameterType, String referredTo, String description) {
            this.name = name;
            this.actionParameterType = actionParameterType;
            this.referredTo = referredTo;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public ParamType getParameterType() {
            return actionParameterType;
        }

        public String getReferredTo() {
            return referredTo;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ParameterMapping)) return false;
            ParameterMapping that = (ParameterMapping) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
