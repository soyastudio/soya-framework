package soya.framework.action;

public interface DynamicAction {

    String[] parameterNames();

    ActionParameterType parameterType(String paramName);

    boolean required(String paramName);

    void setParameter(String paramName, Object value);

    Object getParameter(String paramName);
}