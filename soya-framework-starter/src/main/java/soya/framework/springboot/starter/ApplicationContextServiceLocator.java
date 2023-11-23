package soya.framework.springboot.starter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import soya.framework.commons.io.NamespaceAware;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceLoader;
import soya.framework.commons.io.resource.*;
import soya.framework.context.ServiceLocateException;
import soya.framework.context.ServiceLocator;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContextServiceLocator implements ServiceLocator {
    private ApplicationContext applicationContext;
    private Map<String, ResourceLoader> resourceLoaders = new HashMap<>();
    private ResourceLoader defaultResourceLoader = new URLResourceLoader();

    public ApplicationContextServiceLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        // predefined resource loaders:
        resourceLoaders.put(EnvironmentResource.SCHEMA, EnvironmentResource.loader());
        resourceLoaders.put(ClasspathResource.SCHEMA, ClasspathResource.loader());
        resourceLoaders.put(InvokeResource.SCHEMA, InvokeResource.loader());
        resourceLoaders.put(TodoResource.SCHEMA, TodoResource.loader());

        // additional resource loaders:
        applicationContext.getBeansOfType(ResourceLoader.class).values().forEach(e -> {
            if (e instanceof NamespaceAware) {
                NamespaceAware namespaceAware = (NamespaceAware) e;
                Arrays.stream(namespaceAware.getNamespaces()).forEach(n -> {
                    resourceLoaders.put(n, e);
                });
            }
        });
    }

    @Override
    public String[] serviceNames() {
        return applicationContext.getBeanDefinitionNames();
    }

    @Override
    public Object getService(String name) throws ServiceLocateException {
        try {
            return applicationContext.getBean(name);
        } catch (BeansException e) {
            throw new ServiceLocateException(e);
        }
    }

    @Override
    public <T> T getService(Class<T> type) throws ServiceLocateException {
        try {
            return applicationContext.getBean(type);
        } catch (BeansException e) {
            throw new ServiceLocateException(e);
        }
    }

    @Override
    public <T> T getService(String name, Class<T> type) throws ServiceLocateException {
        try {
            return applicationContext.getBean(name, type);
        } catch (BeansException e) {
            throw new ServiceLocateException(e);
        }
    }

    @Override
    public <T> Map<String, T> getServices(Class<T> type) throws ServiceLocateException {
        try {
            return applicationContext.getBeansOfType(type);
        } catch (BeansException e) {
            throw new ServiceLocateException(e);
        }
    }

    @Override
    public String getProperty(String propName) throws ServiceLocateException {
        return applicationContext.getEnvironment().getProperty(propName);
    }

    @Override
    public Resource getResource(URI uri) throws ServiceLocateException {
        if (resourceLoaders.containsKey(uri.getScheme())) {
            return resourceLoaders.get(uri.getScheme()).load(uri);
        }
        return defaultResourceLoader.load(uri);
    }
}
