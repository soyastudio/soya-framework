package soya.framework.action;

public interface DynaAction {

    String[] parameterNames();

    ActionProperty getActionProperty(String paramName);

    void setParameter(String paramName, Object value);

    Object getParameter(String paramName);

}
