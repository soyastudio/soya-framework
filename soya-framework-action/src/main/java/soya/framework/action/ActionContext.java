package soya.framework.action;

public interface ActionContext {
    String getProperty(String propName, boolean required) throws NotFoundException;

    <T> T getService(String name, Class<T> type) throws NotFoundException;

    <T> T getResource(String url, Class<T> type) throws NotFoundException;

}
