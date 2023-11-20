package soya.framework.commons.io.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;
import soya.framework.commons.util.ReflectUtils;
import soya.framework.commons.util.URIUtils;
import soya.framework.context.ServiceLocatorSingleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class InvokeResource extends AbstractResource<Object> {

    public static final String SCHEMA = "invoke";
    private static final ResourceLoader loader = new InvokeResourceLoader(new String[]{SCHEMA});

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Class<?> clazz;
    private Method method;

    private Map<String, List<String>> params;

    protected InvokeResource(URI uri) {
        super(uri);

        String className = null;
        String methodName = null;
        String queryString = null;

        String string = uri.getSchemeSpecificPart();
        int index = string.indexOf('?');
        if (index > 0) {
            queryString = string.substring(index + 1);
            string = string.substring(0, index);
            params = URIUtils.splitQuery(queryString);
        }

        index = string.indexOf('/');
        if (index > 0) {
            methodName = string.substring(index + 1);
            string = string.substring(0, index);
        }

        className = string;
        try {
            this.clazz = Class.forName(className);
            if (methodName != null) {
                this.method = ReflectUtils.findMethod(clazz, methodName);

            } else if (Callable.class.isAssignableFrom(clazz)) {
                this.method = ReflectUtils.findMethod(clazz, "call");
            }
        } catch (ClassNotFoundException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public Object get() throws ResourceException {
        if (method == null) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ResourceException(e);
            }
        } else if (Modifier.isStatic(method.getModifiers())) {
            try {
                return method.invoke(null, new Object[0]);

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ResourceException(e);
            }

        } else if (Callable.class.isAssignableFrom(clazz)) {
            try {
                Callable callable = (Callable) clazz.newInstance();
                return callable.call();
            } catch (Exception e) {
                throw new ResourceException();
            }
        } else {
            try {
                Object obj = ServiceLocatorSingleton.getInstance().getService(clazz);

                return method.invoke(obj, new Object[0]);

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ResourceException();
            }
        }
    }

    @Override
    public String getAsString(Charset encoding) throws ResourceException {
        Object result = get();
        if (result == null) {
            return null;
        } else if (result instanceof String) {
            return result.toString();
        } else {
            return GSON.toJson(result);
        }
    }

    public static ResourceLoader loader() {
        return loader;
    }

    static class InvokeResourceLoader extends AbstractResourceLoader {
        InvokeResourceLoader(String[] namespaces) {
            super(namespaces);
        }
    }
}
