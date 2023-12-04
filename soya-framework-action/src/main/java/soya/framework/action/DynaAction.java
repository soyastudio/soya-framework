package soya.framework.action;

public interface DynaAction {

    String[] parameterNames();

    Class<?> parameterType(String paramName);

    ActionPropertyType propertyType(String paramName);

    boolean required(String paramName);

    void setParameter(String paramName, Object value);

    Object getParameter(String paramName);

}
