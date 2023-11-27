package soya.framework.action;

import java.util.concurrent.Callable;

public interface ActionFactory<T> {
    Callable<?> create(ActionName actionName, ActionContext actionContext);

}
