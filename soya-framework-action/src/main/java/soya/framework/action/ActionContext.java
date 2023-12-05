package soya.framework.action;

public interface ActionContext {

    String getProperty(String propName, boolean required) throws ActionContextException;

    Object getService(String name) throws ActionContextException;

    <T> T getService(String name, Class<T> type) throws ActionContextException;

    <T> T getResource(String uri, Class<T> type) throws ActionContextException;

}
