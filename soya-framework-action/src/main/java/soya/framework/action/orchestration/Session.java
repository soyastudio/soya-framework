package soya.framework.action.orchestration;

import soya.framework.action.ActionName;

public interface Session {

    ActionName getActionName();

    String getId();

    long getStartTime();

    Object getParameter(String name);

    Object get(String attrName);

    void set(String attrName, Object attrValue);

    Object evaluate(String exp);

}
