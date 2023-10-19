package soya.framework.action;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ActionRegistrationService {

    private Registration defaultRegistration;
    private Map<String, Registration> registrations = new ConcurrentHashMap<>();
    private final AtomicLong lastUpdatedTime;

    ActionRegistrationService(ActionContextInitializer initializer) {

        initializer.getAnnotatedClasses(Domain.class).forEach(e -> {
            ActionClass.createActionDomain(e);

        });

        initializer.getAnnotatedClasses(ActionDefinition.class).forEach(e -> {
            new ActionClass((Class<? extends ActionCallable>) e);
        });

        this.defaultRegistration = new Registration(ActionClass.registry());
        this.lastUpdatedTime = new AtomicLong(System.currentTimeMillis());

        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("========================== refresh registration");
            }
        }, 5000l, 5000l);*/
    }

    public static ActionRegistrationService getInstance() {
        return ActionContext.getInstance().getActionRegistrationService();
    }

    public synchronized long lastUpdatedTime() {
        return lastUpdatedTime.get();
    }

    public long refresh() {
        registrations.entrySet().forEach(e -> {
            Registration registration = e.getValue();
            if (registration.changed()) {
                registration.refresh();
                lastUpdatedTime.set(registration.getRegisterTime());

            } else if (lastUpdatedTime.get() < registration.registerTime) {
                lastUpdatedTime.set(registration.registerTime);
            }
        });

        return lastUpdatedTime.get();
    }

    public synchronized String containsDomain(String name) {
        if (defaultRegistration.containsDomain(name)) {
            return defaultRegistration.registry.id();
        }

        for (Registration registration : registrations.values()) {
            if (registration.containsDomain(name)) {
                return registration.registry.id();
            }
        }

        return null;
    }

    public synchronized String containsAction(ActionName actionName) {
        if (defaultRegistration.containsAction(actionName)) {
            return defaultRegistration.registry.id();
        }

        for (Registration registration : registrations.values()) {
            if (registration.containsAction(actionName)) {
                return registration.registry.id();
            }
        }

        return null;
    }

    public synchronized ActionBean create(ActionName actionName) {
        if (defaultRegistration.containsAction(actionName)) {
            return defaultRegistration.registry.actionFactory().create(actionName);
        }

        for (Registration registration : registrations.values()) {
            if (registration.containsAction(actionName)) {
                return registration.registry.actionFactory().create(actionName);
            }
        }

        throw new IllegalArgumentException("Cannot find action with name: " + actionName);
    }

    // ------------------- Default Registry:
    public synchronized ActionDomain[] domains() {
        return defaultRegistration.domains();
    }

    public synchronized ActionDomain domain(String name) {
        return defaultRegistration.domain(name);
    }

    public synchronized ActionName[] actions() {
        return defaultRegistration.actions();
    }

    public synchronized ActionDescription action(ActionName actionName) {
        return defaultRegistration.actionDescription(actionName);
    }

    // ------------------- Registrations:
    public synchronized void register(ActionRegistry registry) {
        Registration registration = new Registration(registry);
        registrations.put(registry.id(), registration);
        lastUpdatedTime.set(registration.registerTime);
    }

    public synchronized void unregister(String registryId) {
        if (registrations.containsKey(registryId)) {
            registrations.remove(registryId);
            lastUpdatedTime.set(System.currentTimeMillis());
        }
    }

    public synchronized String[] registers() {
        String[] arr = registrations.keySet().toArray(new String[registrations.size()]);
        Arrays.sort(arr);
        return arr;
    }

    public synchronized ActionDomain[] domains(String registryId) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).domains();
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionDomain domain(String registryId, String name) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).domain(name);
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionName[] actions(String registryId) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).actions();
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionDescription action(String registryId, ActionName actionName) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).actionDescription(actionName);
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    private static class Registration {
        private final ActionRegistry registry;

        private Map<String, ActionDomain> domains = new LinkedHashMap<>();
        private Map<ActionName, ActionDescription> actions = new LinkedHashMap<>();
        private long registerTime;

        private Registration(ActionRegistry registry) {
            this.registry = registry;
            refresh();

        }

        private long getRegisterTime() {
            return registerTime;
        }

        private boolean containsDomain(String name) {
            return domains.containsKey(name);
        }

        private ActionDomain[] domains() {
            return domains.values().toArray(new ActionDomain[domains.size()]);
        }

        private ActionDomain domain(String name) {
            return domains.get(name);
        }

        private boolean containsAction(ActionName actionName) {
            return actions.containsKey(actionName);
        }

        private ActionName[] actions() {
            return actions.keySet().toArray(new ActionName[actions.size()]);
        }

        private ActionDescription actionDescription(ActionName actionName) {
            return actions.get(actionName);
        }

        private boolean changed() {
            return registry.lastUpdatedTime() > registerTime;
        }

        private void refresh() {
            this.domains.clear();
            List<ActionDomain> domainList = new ArrayList<>(registry.domains());
            Collections.sort(domainList);
            domainList.forEach(e -> {
                domains.put(e.getName(), e);
            });

            this.actions.clear();
            List<ActionDescription> actionList = new ArrayList<>(registry.actions());
            Collections.sort(actionList);
            actionList.forEach(e -> {
                actions.put(e.getActionName(), e);
            });

            this.registerTime = System.currentTimeMillis();
        }
    }
}
