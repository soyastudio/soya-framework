package soya.framework.action.springboot;

import soya.framework.action.ActionContext;
import soya.framework.action.ActionContextException;
import soya.framework.commons.conversion.ConvertUtils;
import soya.framework.commons.io.Resource;
import soya.framework.context.ServiceLocateException;
import soya.framework.context.ServiceLocator;
import soya.framework.context.ServiceLocatorSingleton;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class SpringActionContext implements ActionContext {

    @Override
    public String getProperty(String propName, boolean required) throws ActionContextException {
        try {
            String property = serviceLocator().getProperty(propName);
            if (property == null && required) {
                throw new ActionContextException("Required property is not found: " + propName);
            }

            return property;

        } catch (ServiceLocateException e) {
            if(required) {
                throw new ActionContextException("Required property is not found: " + propName);
            }

            return null;
        }
    }

    @Override
    public <T> T getService(String name, Class<T> type) throws ActionContextException {
        try {
            if (name == null || name.isEmpty()) {
                return serviceLocator().getService(type);
            } else {
                return serviceLocator().getService(name, type);
            }
        } catch (Exception e) {
            throw new ActionContextException(e);
        }
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        ServiceLocator locator = ServiceLocatorSingleton.getInstance();
        try {
            Resource resource = locator.getResource(new URI(url));
            if (type.isInstance(resource)) {
                return (T) resource;

            } else if (String.class.isAssignableFrom(type)) {
                return (T) resource.getAsString(Charset.defaultCharset());

            } else if (loadable(resource.getClass(), type)) {
                return (T) resource.get();

            } else {
                return (T) ConvertUtils.convert(resource.getAsString(Charset.defaultCharset()), type);

            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean loadable(Class<? extends Resource> resourceType, Class<?> dataType) {
        if (((ParameterizedType) resourceType.getGenericSuperclass())
                .getActualTypeArguments().length > 0) {
            Class<?> parameterizedType = (Class<?>) ((ParameterizedType) resourceType.getGenericSuperclass())
                    .getActualTypeArguments()[0];
            return dataType.isAssignableFrom(parameterizedType);
        }
        return false;
    }

    private ServiceLocator serviceLocator() {
        return ServiceLocatorSingleton.getInstance();
    }
}
