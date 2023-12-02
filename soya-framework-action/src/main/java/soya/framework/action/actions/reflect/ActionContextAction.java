package soya.framework.action.actions.reflect;

import soya.framework.action.ActionContext;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.concurrent.Callable;

public abstract class ActionContextAction implements Callable<String> {

    protected ActionContext actionContext() {
        return ServiceLocatorSingleton.getInstance().getService(ActionContext.class);
    }
}
