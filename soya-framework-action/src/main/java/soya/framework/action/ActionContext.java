package soya.framework.action;

public interface ActionContext {
    String getProperty(String propName);

    Object getService(String name) throws ServiceNotFoundException;

    <T> T getService(String name, Class<T> type) throws ServiceNotFoundException;

    <T> T getResource(String url, Class<T> type);

}
