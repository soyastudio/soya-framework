package soya.framework.action;

import soya.framework.action.util.ConvertUtils;
import soya.framework.commons.io.NamespaceAware;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceLoader;
import soya.framework.commons.io.resource.ClasspathResourceLoader;
import soya.framework.commons.io.resource.URLResourceLoader;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Callable;

public final class SimpleActionExecutor implements ActionExecutor {
    private DefaultActionContext actionContext = new DefaultActionContext();
    private static SimpleActionExecutor me;

    static {
        me = new SimpleActionExecutor();
    }

    private SimpleActionExecutor() {
        actionContext = new DefaultActionContext();
        register(new ClasspathResourceLoader());
    }

    public static SimpleActionExecutor getInstance() {
        return me;
    }

    public SimpleActionExecutor register(Class<? extends Callable>... cls) {
        Arrays.stream(cls).forEach(c -> {
            ActionClass.register(c);
        });

        return this;
    }

    public SimpleActionExecutor register(Properties properties) {
        me.actionContext.properties.putAll(properties);
        return this;
    }

    public SimpleActionExecutor register(String name, Object service) {
        me.actionContext.services.put(name, service);
        return this;
    }

    public SimpleActionExecutor register(ResourceLoader resourceLoader) {
        if (resourceLoader instanceof NamespaceAware) {
            String[] ns = ((NamespaceAware) resourceLoader).getNamespaces();
            Arrays.stream(ns).forEach(e -> {
                me.actionContext.resourceLoaders.put(e, resourceLoader);
            });
        }
        return this;
    }

    @Override
    public Object execute(String command) {
        return null;
    }

    private static class DefaultActionContext implements ActionContext {
        private Properties properties = new Properties();
        private Map<String, Object> services = new HashMap<>();

        private Map<String, ResourceLoader> resourceLoaders = new HashMap<>();
        private ResourceLoader defaultResourceLoader = new URLResourceLoader();

        private DefaultActionContext() {
        }

        @Override
        public String getProperty(String propName) {
            String value = properties.getProperty(propName);
            if (value == null) {
                value = System.getProperty(propName);
            }
            return value;
        }

        @Override
        public Object getService(String name) {
            return services.get(name);
        }

        @Override
        public <T> T getService(String name, Class<T> type) {
            if (name != null) {
                return (T) services.get(name);
            } else {
                Iterator<?> iterator = services.values().iterator();
                while (iterator.hasNext()) {
                    Object o = iterator.next();
                    if (type.isInstance(o)) {
                        return (T) o;
                    }
                }
                throw new NullPointerException("Cannot find service of type: " + type.getName());
            }
        }

        @Override
        public <T> T getResource(String url, Class<T> type) {
            Resource resource = null;
            if (url.contains(":")) {
                String ns = url.substring(0, url.indexOf(':'));
                if (resourceLoaders.containsKey(ns)) {
                    resource = resourceLoaders.get(ns).load(url);
                }
            }

            if (resource == null) {
                resource = defaultResourceLoader.load(url);
            }

            return (T) ConvertUtils.convert(resource.getAsString(Charset.defaultCharset()), type);
        }
    }
}
