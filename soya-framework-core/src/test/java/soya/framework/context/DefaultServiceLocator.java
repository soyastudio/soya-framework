package soya.framework.context;

import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceLoader;
import soya.framework.commons.io.resource.URLResourceLoader;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DefaultServiceLocator extends ServiceLocatorSingleton {
    private static Locator registration;

    static {
        registration = new Locator();
        new DefaultServiceLocator(registration);
    }

    protected DefaultServiceLocator(Locator locator) {
        super(locator);
    }

    public static void addProperty(String key, String value) {
        registration.properties.setProperty(key, value);
    }

    public static void addResourceLoader(String schema, ResourceLoader resourceLoader) {
        registration.resourceLoaders.put(schema, resourceLoader);
    }

    static class Locator implements ServiceLocator {

        private Properties properties = new Properties();
        private Map<String, ResourceLoader> resourceLoaders = new HashMap<>();
        private ResourceLoader defaultResourceLoader = new URLResourceLoader();

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
            if (properties.containsKey(propName)) {
                return properties.getProperty(propName);
            } else {
                throw new ServiceLocateException("Property does not exist: " + propName);
            }

        }

        @Override
        public Resource getResource(URI uri) throws ServiceLocateException {
            if (resourceLoaders.containsKey(uri.getScheme())) {
                return resourceLoaders.get(uri.getScheme()).load(uri);
            }
            return defaultResourceLoader.load(uri);
        }
    }
}
