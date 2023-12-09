package soya.framework.action.orchestration;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionParameter;
import soya.framework.action.ActionProperty;
import soya.framework.action.DynaAction;
import soya.framework.context.ServiceLocatorSingleton;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class DynaActionBase<T> implements DynaAction, Callable<T> {

    protected Map<String, ActionParameter> parameters = new LinkedHashMap<>();

    @Override
    public String[] parameterNames() {
        return parameters.keySet().toArray(new String[parameters.size()]);
    }

    @Override
    public ActionProperty getActionProperty(String paramName) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("Parameter is not defined: " + paramName);
        }
        return parameters.get(paramName).getProperty();
    }

    @Override
    public Object getParameter(String paramName) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("Parameter is not defined: " + paramName);
        }
        return parameters.get(paramName).get();
    }

    @Override
    public void setParameter(String paramName, Object value) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("Parameter is not defined: " + paramName);
        }
        parameters.get(paramName).set(value);
    }

    protected ActionContext getActionContext() {
        return ServiceLocatorSingleton.getInstance().getService(ActionContext.class);
    }
}
