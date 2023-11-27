package soya.framework.action;

public class ActionParameter {

    private final ActionProperty property;
    private Object value;

    public ActionParameter(ActionProperty property) {
        this.property = property;
    }

    public String getName() {
        return property.getName();
    }

    public Class<?> getType() {
        return property.getType();
    }

    public ActionParameterType getParameterType() {
        return property.getParameterType();
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
        this.value = value;
    }
}
