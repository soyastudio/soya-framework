package soya.framework.action;

public interface ActionRegistration {
    Class<?> getActionType(String actionName);
}
