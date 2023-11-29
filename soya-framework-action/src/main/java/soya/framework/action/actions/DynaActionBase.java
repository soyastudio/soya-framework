package soya.framework.action.actions;

import soya.framework.action.ActionParameter;
import soya.framework.action.ActionPropertyType;
import soya.framework.action.DynaAction;
import soya.framework.commons.conversion.ConvertUtils;

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
    public ActionPropertyType parameterType(String paramName) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("Parameter is not defined: " + paramName);
        }
        return parameters.get(paramName).getParameterType();
    }

    @Override
    public boolean required(String paramName) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("Parameter is not defined: " + paramName);
        }
        return parameters.get(paramName).isRequired();
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
        ActionParameter parameter = parameters.get(paramName);
        parameters.get(paramName).set(ConvertUtils.convert(value, parameter.getType()));
    }
}
