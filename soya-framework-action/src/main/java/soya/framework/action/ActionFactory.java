package soya.framework.action;

public interface ActionFactory {
    ActionBean create(ActionName actionName);
}
