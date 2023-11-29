package soya.framework.action;

import java.io.Serializable;
import java.util.Objects;

public final class ActionProperty implements Serializable {

    private final String name;
    private final Class<?> type;
    private final ActionParameterType parameterType;
    private final String referredTo;
    private final boolean required;
    private final String description;

    private ActionProperty(String name, Class<?> type, ActionParameterType parameterType, String referredTo, boolean required, String description) {
        this.name = name;
        this.type = type;
        this.parameterType = parameterType;
        this.referredTo = referredTo;
        this.required = required;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public ActionParameterType getParameterType() {
        return parameterType;
    }

    public String getReferredTo() {
        return referredTo;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private Class<?> type;
        private ActionParameterType parameterType;
        private String referredTo;
        private boolean required;
        private String description;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(Class<?> type) {
            this.type = type;
            return this;
        }

        public Builder parameterType(ActionParameterType parameterType) {
            this.parameterType = parameterType;
            return this;
        }

        public Builder referredTo(String referredTo) {
            this.referredTo = referredTo;
            return this;
        }

        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ActionProperty create() {
            return new ActionProperty(name, type, parameterType, referredTo, required, description);
        }
    }
}
