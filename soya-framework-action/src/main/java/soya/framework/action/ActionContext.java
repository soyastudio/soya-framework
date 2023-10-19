package soya.framework.action;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ActionContext {

    private static ActionContext INSTANCE;

    private final Properties properties;
    private final ExecutorService executorService;
    private final ServiceLocator serviceLocator;
    private final ActionRegistrationService actionRegistrationService;

    protected ActionContext(ActionContextInitializer initializer) {
        this.properties = initializer.getProperties();
        this.serviceLocator = initializer.getServiceLocator();
        this.executorService = createExecutorService();
        this.actionRegistrationService = new ActionRegistrationService(initializer);

        INSTANCE = this;
    }

    protected ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    public Map<String, String> properties() {
        Map<String, String> map = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            keys.add((String) enumeration.nextElement());
        }
        Collections.sort(keys);

        keys.forEach(e -> {
            map.put(e, properties.getProperty(e));
        });

        return map;

    }

    public String getProperty(String key) {
        if (properties.contains(key)) {
            return properties.getProperty(key);

        } else if (System.getProperty(key) != null) {
            return System.getProperty(key);

        } else {
            return null;
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ActionRegistrationService getActionRegistrationService() {
        return actionRegistrationService;
    }

    public String[] serviceNames() {
        return serviceLocator.serviceNames();
    }

    public Object getService(String name) throws ServiceLocateException {
        return serviceLocator.getService(name);
    }

    public <T> T getService(Class<T> type) throws ServiceLocateException {
        return serviceLocator.getService(type);
    }

    public <T> T getService(String name, Class<T> type) throws ServiceLocateException {
        return serviceLocator.getService(name, type);
    }

    public <T> Map<String, T> getServices(Class<T> type) throws ServiceLocateException {
        return serviceLocator.getServices(type);
    }

    public static ActionContext getInstance() {
        return INSTANCE;
    }



}
