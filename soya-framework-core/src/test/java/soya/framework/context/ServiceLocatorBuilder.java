package soya.framework.context;

import soya.framework.commons.io.Resource;

import java.net.URI;
import java.util.Map;

class ServiceLocatorBuilder {

    ServiceLocatorBuilder() {
    }

    ServiceLocator create() {
        return new DefaultServiceLocator();
    }

    static class DefaultServiceLocator implements ServiceLocator {

        @Override
        public String[] serviceNames() {
            return new String[0];
        }

        @Override
        public Object getService(String name) throws ServiceLocateException {
            return null;
        }

        @Override
        public <T> T getService(Class<T> type) throws ServiceLocateException {
            return null;
        }

        @Override
        public <T> T getService(String name, Class<T> type) throws ServiceLocateException {
            return null;
        }

        @Override
        public <T> Map<String, T> getServices(Class<T> type) throws ServiceLocateException {
            return null;
        }

        @Override
        public String getProperty(String propName) throws ServiceLocateException {
            return null;
        }

        @Override
        public Resource getResource(URI uri) throws ServiceLocateException {
            return null;
        }
    }
}
