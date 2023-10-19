package soya.framework.action;

import java.util.Map;

public interface ServiceLocator {
    String[] serviceNames();

    Object getService(String name) throws ServiceLocateException;

    <T> T getService(Class<T> type) throws ServiceLocateException;

    <T> T getService(String name, Class<T> type) throws ServiceLocateException;

    <T> Map<String, T> getServices(Class<T> type) throws ServiceLocateException;
}
