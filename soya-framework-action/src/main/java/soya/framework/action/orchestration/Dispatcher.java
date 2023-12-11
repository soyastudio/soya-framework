package soya.framework.action.orchestration;

import soya.framework.action.ActionContext;

public interface Dispatcher {
    Object dispatch(Session session) throws Exception;
}
