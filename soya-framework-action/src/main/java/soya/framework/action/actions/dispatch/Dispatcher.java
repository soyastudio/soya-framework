package soya.framework.action.actions.dispatch;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionParameter;

import java.util.Map;

public interface Dispatcher<T> {
    T dispatch(Map<String, ActionParameter> parameters, ActionContext actionContext) throws Exception;
}
