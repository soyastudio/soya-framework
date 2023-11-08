package soya.framework.restruts.springboot;

import org.springframework.context.ApplicationContext;
import soya.framework.restruts.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

class DefaultRestActionContext implements RestActionContext, DependentInjector {
    private ApplicationContext applicationContext;
    private Map<ActionMapping.Path, ActionMapping> registrations = new HashMap();
    private List<ActionMapping> pathMappings = new ArrayList<>();
    private DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
    private Map<String, Serializer> serializerMap = new HashMap<>();

    private DefaultExceptionHandler exceptionHandler = new DefaultExceptionHandler();

    private String apiPath;
    private String api;

    public DefaultRestActionContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String getProperty(String propName) {
        return applicationContext.getEnvironment().getProperty(propName);
    }

    @Override
    public Object getService(String name) {
        try{
            return applicationContext.getBean(name);
        } catch (Exception e) {
            try {
                return applicationContext.getBean(Class.forName(name));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public <T> T getService(String name, Class<T> type) {
        return applicationContext.getBean(name, type);
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        return resourceLoader.getResource(url, type);
    }


    public Serializer getSerializer(String mediaType) {
        String key = mediaType.toUpperCase();
        if (serializerMap.containsKey(key)) {
            return serializerMap.get(key);
        } else {
            return new DefaultSerializer();
        }
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public ActionMapping[] getActionMappings() {
        List<ActionMapping> list = new ArrayList<>(registrations.values());
        Collections.sort(list);
        return list.toArray(new ActionMapping[list.size()]);
    }

    public ActionMapping getActionMapping(HttpServletRequest request) {
        ActionMapping.Path key = ActionMapping.path(request.getMethod(), request.getPathInfo());
        if (registrations.containsKey(key)) {
            return registrations.get(key);
        } else {
            Iterator<ActionMapping> iterator = pathMappings.iterator();
            while (iterator.hasNext()) {
                ActionMapping next = iterator.next();
                if (next.getPath().equals(key)) {
                    return next;
                }
            }
            return null;
        }
    }

    @Override
    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    DefaultRestActionContext register(ResourceLoader loader) {
        if(loader instanceof RestActionContextAware) {
            ((RestActionContextAware) loader).setContext(this);
        }
        resourceLoader.add((NamespaceResourceLoader) loader);
        return this;
    }

    DefaultRestActionContext register(String mediaType, Serializer serializer) {
        if(serializer instanceof RestActionContextAware) {
            ((RestActionContextAware) serializer).setContext(this);
        }
        serializerMap.put(mediaType.toUpperCase(), serializer);
        return this;
    }

    DefaultRestActionContext register(RestActionLoader loader) {
        loader.load().forEach(e -> {
            this.register(e);
        });
        return this;
    }

    DefaultRestActionContext register(ActionMapping mapping) {
        registrations.put(mapping.getPath(), mapping);
        if (mapping.isPathMapping()) {
            pathMappings.add(mapping);
        }
        return this;
    }

    @Override
    public Map<String, String> getServiceRegistrations() {
        Map<String, String> map = new HashMap<>();
        String[] arr =  applicationContext.getBeanDefinitionNames();
        Arrays.stream(arr).forEach(e -> {
            map.put(e, applicationContext.getBean(e).getClass().getName());
        });
        return map;
    }

    static class DefaultSerializer implements Serializer {
        @Override
        public <T> byte[] serialize(T o) throws SerializationException {
            return o.toString().getBytes();
        }

        @Override
        public <T> T deserialize(byte[] data, Class<T> type) throws SerializationException {
            return (T) new String(data);
        }
    }

}
