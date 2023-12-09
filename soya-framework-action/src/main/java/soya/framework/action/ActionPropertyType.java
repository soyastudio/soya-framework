package soya.framework.action;

public enum ActionPropertyType {
    PARAM, INPUT, WIRED_VALUE, WIRED_SERVICE, WIRED_PROPERTY, WIRED_RESOURCE;

    public final boolean isWired() {
        return name().startsWith("WIRED_");
    }
}
