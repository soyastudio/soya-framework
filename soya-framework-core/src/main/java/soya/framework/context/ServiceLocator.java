package soya.framework.context;

import soya.framework.commons.io.Resource;

import java.net.URI;
import java.util.Map;

public interface ServiceLocator {
    String[] serviceNames();

    Object getService(String name) throws ServiceLocateException;

    <T> T getService(Class<T> type) throws ServiceLocateException;

    <T> T getService(String name, Class<T> type) throws ServiceLocateException;

    <T> Map<String, T> getServices(Class<T> type) throws ServiceLocateException;

    String getProperty(String propName, boolean required) throws ServiceLocateException;

    Resource getResource(URI uri) throws ServiceLocateException;
}

