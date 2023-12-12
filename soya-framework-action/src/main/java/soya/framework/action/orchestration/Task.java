package soya.framework.action.orchestration;

import soya.framework.action.ActionClass;
import soya.framework.action.orchestration.annotation.TaskDefinition;
import soya.framework.commons.util.URIUtils;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public final class Task implements Serializable {
    private final String name;
    private final URI uri;
    private final ParameterMapping[] parameterMappings;

    private Task(String name, URI uri, ParameterMapping[] parameterMappings) {
        this.name = name;
        this.uri = uri;
        this.parameterMappings = parameterMappings;
    }

    public String getName() {
        return name;
    }

    public URI getUri() {
        return uri;
    }

    public ParameterMapping[] getParameterMappings() {
        return parameterMappings;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(TaskDefinition taskDefinition) {
        Builder builder = new Builder().name(taskDefinition.name())
                .uri(taskDefinition.uri());
        Arrays.stream(taskDefinition.parameterMappings()).forEach(e -> {
           builder.parameterMappings.add(new ParameterMapping(e.name(), e.type(), e.mappingTo()));
        });

        return builder;
    }

    public static Builder builder(Class<? extends Callable> actionType) {
        Builder builder = new Builder();
        builder.uri = ActionClass.forClass(actionType).getName().toURI();
        return builder;
    }

    public static class Builder {
        private String name;
        private URI uri;

        private List<ParameterMapping> parameterMappings = new ArrayList<>();

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder uri(String uri) {
            try {
                if(!uri.contains("?")) {
                    this.uri = new URI(uri);
                } else {
                    int index = uri.indexOf('?');
                    this.uri = new URI(uri.substring(0, index));
                    String queryString = uri.substring(index + 1);
                    URIUtils.splitQuery(queryString).entrySet().forEach(e -> {
                        parameterMappings.add(new ParameterMapping(e.getKey(), e.getValue().get(0)));
                    });
                }
                return this;

            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public Task create() {
            return new Task(name, uri, parameterMappings.toArray(new ParameterMapping[parameterMappings.size()]));
        }
    }

    public static class ParameterMapping {
        private String name;
        private Class<?> type;
        private String mappingTo;

        ParameterMapping(String name, String mappingTo) {
            this.name = name;
            this.mappingTo = mappingTo;
        }

        ParameterMapping(String name, Class<?> type, String mappingTo) {
            this.name = name.isEmpty() ? mappingTo : name;
            this.type = type;
            this.mappingTo = mappingTo;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public String getMappingTo() {
            return mappingTo;
        }
    }
}
