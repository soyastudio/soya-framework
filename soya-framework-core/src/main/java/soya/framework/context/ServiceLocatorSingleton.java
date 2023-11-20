package soya.framework.context;

import soya.framework.commons.io.Resource;

import java.net.URI;
import java.util.Map;

public abstract class ServiceLocatorSingleton implements ServiceLocator {
    private static ServiceLocatorSingleton me;
    private ServiceLocator locator;

    protected ServiceLocatorSingleton(ServiceLocator locator) {
        if (this.locator != null) {
            throw new IllegalStateException("ServiceLocator already exist!");
        } else {
            this.locator = locator;
        }

        me = this;
    }

    public static ServiceLocator getInstance() {
        return me;
    }

    @Override
    public String[] serviceNames() {
        return locator.serviceNames();
    }

    @Override
    public Object getService(String name) throws ServiceLocateException {
        return locator.getService(name);
    }

    @Override
    public <T> T getService(Class<T> type) throws ServiceLocateException {
        return locator.getService(type);
    }

    @Override
    public <T> T getService(String name, Class<T> type) throws ServiceLocateException {
        return locator.getService(name, type);
    }

    @Override
    public <T> Map<String, T> getServices(Class<T> type) throws ServiceLocateException {
        return locator.getServices(type);
    }

    @Override
    public String getProperty(String propName) throws ServiceLocateException {
        return locator.getProperty(propName);
    }

    @Override
    public Resource getResource(URI uri) throws ServiceLocateException {
        return locator.getResource(uri);
    }

}
