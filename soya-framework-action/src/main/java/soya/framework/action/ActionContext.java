package soya.framework.action;

public interface ActionContext {
    String getProperty(String propName);

    Object getService(String name);

    <T> T getService(String name, Class<T> type);

    <T> T getResource(String url, Class<T> type);

}
