package soya.framework.action;

import java.io.Serializable;

public final class ActionProperty implements Serializable {

    private final String name;
    private final transient Class<?> _type;
    private final String type;

    private final ActionPropertyType propertyType;
    private final String referredTo;
    private final boolean required;
    private final String description;

    private ActionProperty(String name, Class<?> type, ActionPropertyType propertyType, String referredTo, boolean required, String description) {
        this.name = name;
        this._type = type;
        this.type = type.getName();
        this.propertyType = propertyType;
        this.referredTo = referredTo;
        this.required = required;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return _type;
    }

    public ActionPropertyType getPropertyType() {
        return propertyType;
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
        private ActionPropertyType propertyType;
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

        public Builder propertyType(ActionPropertyType propertyType) {
            this.propertyType = propertyType;
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
            return new ActionProperty(name, type, propertyType, referredTo, required, description);
        }
    }
}
