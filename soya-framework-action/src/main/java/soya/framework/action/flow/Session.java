package soya.framework.action.flow;

import soya.framework.action.ActionName;

public interface Session {

    ActionName getActionName();

    String getId();

    long startTime();

    Object getParameter(String name);

    Object get(String attrName);

    void set(String attrName, Object attrValue);

}
