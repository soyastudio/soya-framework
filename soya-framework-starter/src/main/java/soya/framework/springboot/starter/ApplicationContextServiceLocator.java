package soya.framework.springboot.starter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.resource.*;
import soya.framework.context.ServiceLocateException;
import soya.framework.context.ServiceLocator;

import java.net.URI;
import java.util.Map;

public class ApplicationContextServiceLocator implements ServiceLocator {
    private ApplicationContext applicationContext;

    private DefaultResourceLoader resourceLoader;

    public ApplicationContextServiceLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        this.resourceLoader = (DefaultResourceLoader) DefaultResourceLoader.getInstance();
        resourceLoader.register(EnvironmentResource.SCHEMA, EnvironmentResource.loader())
                .register(ClasspathResource.SCHEMA, ClasspathResource.loader())
                .register(InvokeResource.SCHEMA, InvokeResource.loader())
                .register(TodoResource.SCHEMA, TodoResource.loader());

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
        String prop = applicationContext.getEnvironment().getProperty(propName);
        if (prop == null) {
            throw new ServiceLocateException("Property is not found: " + propName);
        } else {
            return prop;
        }
    }

    @Override
    public Resource getResource(URI uri) throws ServiceLocateException {
        try {
            return resourceLoader.load(uri);

        } catch (ResourceException e) {
            throw new ServiceLocateException(e);
        }
    }
}
