package soya.framework.action;

import soya.framework.commons.conversion.ConvertUtils;
import soya.framework.commons.util.DefaultUtils;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public final class ActionClass {

    private static Logger logger = Logger.getLogger(ActionClass.class.getName());

    private static Map<ActionName, ActionClass> registrations = new LinkedHashMap<>();
    private static Map<ActionName, ActionFactory> creators = new HashMap<>();

    private static Map<Class<?>, ActionFactory> factories = new HashMap<>();
    private static DefaultActionFactory defaultActionFactory = new DefaultActionFactory();

    private final transient Class<? extends Callable> actionType;
    private final transient ActionProperty[] interactiveProperties;

    private final String action;
    private final ActionName name;
    private final ActionProperty[] properties;

    private ActionClass(Class<? extends Callable> actionType, ActionName name, ActionProperty[] actionProperties) {
        this.name = name;
        this.actionType = actionType;
        this.action = actionType.getName();
        this.properties = actionProperties;

        List<ActionProperty> interactive = new ArrayList<>();
        Arrays.stream(actionProperties).forEach(e -> {
            if (!e.getPropertyType().isWired()) {
                interactive.add(e);
            }
        });

        this.interactiveProperties = interactive.toArray(new ActionProperty[interactive.size()]);
    }

    public Class<? extends Callable> getActionType() {
        return actionType;
    }

    public ActionName getName() {
        return name;
    }

    public ActionProperty[] getProperties() {
        return properties;
    }

    static void register(ActionFactory factory) {
        Class<?> type = (Class<?>) ((ParameterizedType) factory.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        factories.put(type, factory);
    }

    private static void register(ActionClass actionClass) {
        ActionName actionName = actionClass.getName();
        if (!registrations.containsKey(actionName)) {
            registrations.put(actionName, actionClass);
        }
        creators.put(actionName, findActionFactory(actionClass.getActionType()));
    }

    private static ActionFactory findActionFactory(Class<?> cls) {
        for (Map.Entry<Class<?>, ActionFactory> entry : factories.entrySet()) {
            if (entry.getKey().isAssignableFrom(cls)) {
                return entry.getValue();
            }
        }
        return defaultActionFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ActionExecutor<?> executor(ActionContext actionContext) {
        return new ActionExecutor(name, creators.get(name).create(name, actionContext), interactiveProperties);
    }

    public static ActionName[] actionNames() {
        List<ActionName> actionNames = new ArrayList<>(registrations.keySet());
        Collections.sort(actionNames);
        return actionNames.toArray(new ActionName[registrations.size()]);
    }

    public static ActionClass forName(ActionName actionName) {
        if (!registrations.containsKey(actionName)) {
            throw new IllegalArgumentException("Action does not exist: " + actionName);
        }

        return registrations.get(actionName);
    }

    static class DefaultActionFactory implements ActionFactory {

        @Override
        public Callable<?> create(ActionName actionName, ActionContext actionContext) {
            try {
                ActionClass actionClass = ActionClass.forName(actionName);
                Class<?> actionType = actionClass.getActionType();
                Callable<?> callable = (Callable<?>) actionType.newInstance();
                Arrays.stream(actionClass.getProperties()).forEach(e -> {
                    if (e.getPropertyType().isWired()) {
                        setProperty(e.getName(), wiredValue(e, actionContext), callable);
                    }
                });

                return callable;

            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private void setProperty(String name, Object value, Callable<?> callable) {
            if (value == null) {
                return;

            } else if (callable instanceof DynaAction) {
                DynaAction dynaAction = (DynaAction) callable;
                dynaAction.setParameter(name, value);

            } else {
                try {
                    Field field = ReflectUtils.findField(callable.getClass(), name);
                    field.setAccessible(true);
                    field.set(callable, value);

                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        private Object wiredValue(ActionProperty property, ActionContext actionContext) {
            try {
                if (ActionPropertyType.WIRED_VALUE.equals(property.getPropertyType())) {
                    return ConvertUtils.convert(property.getReferredTo(), property.getType());

                } else if (ActionPropertyType.WIRED_PROPERTY.equals(property.getPropertyType())) {
                    return ConvertUtils.convert(actionContext.getProperty(property.getReferredTo(), property.isRequired()), property.getType());

                } else if (ActionPropertyType.WIRED_SERVICE.equals(property.getPropertyType())) {
                    return actionContext.getService(property.getReferredTo(), property.getType());

                } else if (ActionPropertyType.WIRED_RESOURCE.equals(property.getPropertyType())) {
                    return actionContext.getResource(property.getReferredTo(), property.getType());

                } else {
                    return null;

                }
            } catch (ActionContextException ex) {
                throw new RuntimeException(ex);

            }
        }
    }

    public static class Builder {

        private Class<? extends Callable> actionType;
        private ActionName actionName;
        private List<ActionProperty> properties = new ArrayList<>();

        private Builder() {
        }

        public Builder actionType(Class<? extends Callable> type) {
            this.actionType = type;
            return this;
        }

        public Builder actionName(ActionName actionName) {
            this.actionName = actionName;
            return this;
        }

        public Builder addProperty(ActionProperty property) {
            this.properties.add(property);
            return this;
        }

        public Builder addProperty(Field field, ActionPropertyDefinition annotation) {
            this.properties.add(ActionProperty.builder()
                    .name(field.getName())
                    .type(DefaultUtils.isDefaultType(annotation.type()) ? field.getType() : DefaultUtils.getDefaultType(field.getType()))
                    .propertyType(annotation.propertyType())
                    .referredTo(annotation.referredTo())
                    .required(annotation.required())
                    .description(annotation.description())
                    .create());
            return this;
        }

        public ActionClass create() {
            Objects.requireNonNull(actionName);
            Objects.requireNonNull(actionType);

            if (registrations.containsKey(actionName)) {
                throw new IllegalArgumentException("ActionClass already exists: " + actionName);
            }

            ActionClass actionClass = new ActionClass(actionType, actionName, properties.toArray(new ActionProperty[properties.size()]));
            register(actionClass);

            return actionClass;
        }

    }
}
