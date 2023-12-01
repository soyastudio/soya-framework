package soya.framework.action;

import soya.framework.commons.conversion.ConvertUtils;

public final class ActionParameter {

    private final ActionProperty property;
    private Object value;

    public ActionParameter(ActionProperty property) {
        this.property = property;
    }

    public String getName() {
        return property.getName();
    }

    public Class<?> getType() {
        return property.get_type();
    }

    public ActionPropertyType getParameterType() {
        return property.getPropertyType();
    }

    public String getReferredTo() {
        return property.getReferredTo();
    }

    public boolean isRequired() {
        return property.isRequired();
    }

    public String getDescription() {
        return property.getDescription();
    }

    public Object get() {
        return value;
    }

    public void set(Object value) {
        this.value = ConvertUtils.convert(value, property.get_type());
    }

    public void reset() {
        this.value = null;
    }
}
