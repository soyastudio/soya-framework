package soya.framework.restruts.mappings;


import soya.framework.restruts.HttpParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ParameterMapping implements Serializable {
    private final String name;
    private final HttpParam actionParameterType;
    private List<String> descriptions = new ArrayList<>();
    private String contentType = "text/plain";

    public ParameterMapping(String name, HttpParam actionParameterType) {
        this.name = name;
        this.actionParameterType = actionParameterType;
    }

    public String getName() {
        return name;
    }

    public HttpParam getParameterType() {
        return actionParameterType;
    }

    public void addDescriptions(String... lines) {
        if (lines != null) {
            descriptions.addAll(Arrays.asList(lines));
        }
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        descriptions.forEach(e -> {
            builder.append(e).append("\n");
        });
        return builder.toString();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
