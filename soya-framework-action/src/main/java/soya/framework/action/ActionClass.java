package soya.framework.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.util.ConvertUtils;
import soya.framework.action.util.ReflectUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public final class ActionClass {
    private static Logger logger = Logger.getLogger(ActionClass.class.getName());
    private static Map<ActionName, ActionClass> registrations = new LinkedHashMap<>();
    private final ActionName actionName;
    private final Class<? extends Callable> actionType;

    private Map<String, Property> propertyMap = new LinkedHashMap<>();

    private ActionClass(Class<? extends Callable> actionType) {
        ActionDefinition annotation = actionType.getAnnotation(ActionDefinition.class);
        if (annotation == null) {
            throw new IllegalArgumentException();
        }
        this.actionName = new ActionName(annotation.domain(), annotation.name());
        this.actionType = actionType;

        Field[] fields = ReflectUtils.getFields(actionType);
        Arrays.stream(fields).forEach(field -> {
            if (!Modifier.isFinal(field.getModifiers())
                    && !Modifier.isStatic(field.getModifiers())) {
                propertyMap.put(field.getName(), new Property(field));
            }
        });

        Arrays.stream(annotation.parameters()).forEach(e -> {
            if (!propertyMap.containsKey(e.name())) {
                throw new IllegalArgumentException();
            }
            Property property = propertyMap.get(e.name());
            property.set(e.type(), e.referredTo(), e.required(), e.description());
        });

        if (registrations.containsKey(actionName)) {
            throw new IllegalArgumentException("Action has already registered with name '" + actionName + "'.");
        } else {
            registrations.put(actionName, this);
            logger.info("Registered action " + actionType.getName() + " as " + actionName);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            logger.info(gson.toJson(forName(actionName).propertyMap));
        }
    }

    public static void register(Class<? extends Callable> cls) {
        new ActionClass(cls);
    }

    public static ActionClass forName(ActionName actionName) {
        if (!registrations.containsKey(actionName)) {
            throw new IllegalArgumentException("Action does not exist: " + actionName);
        }

        return registrations.get(actionName);
    }

    public ActionExecutor<?> newInstance(ActionContext actionContext) {
        try {
            Callable<?> instance = registrations.get(actionName).actionType.newInstance();
            ActionExecutor<?> task = new ActionExecutor<>(instance);
            propertyMap.values().forEach(p -> {
                Field field = p.getField();
                Object value = null;
                if (p.type.equals(ActionParameterType.INPUT)) {
                    task.addParameter(field, p.referredTo, p.required);

                } else if (p.type.equals(ActionParameterType.WIRED_SERVICE)) {
                    if (p.getReferredTo().trim().isEmpty()) {
                        value = actionContext.getService(null, field.getType());
                    } else {
                        value = actionContext.getService(p.getReferredTo(), field.getType());
                    }

                } else if (p.type.equals(ActionParameterType.WIRED_PROPERTY)) {
                    if (p.getReferredTo() == null) {
                        throw new IllegalArgumentException("'referredTo' is required.");
                    }
                    value = ConvertUtils.convert(actionContext.getProperty(p.getReferredTo()), field.getType());

                } else if (p.type.equals(ActionParameterType.WIRED_RESOURCE)) {
                    value = actionContext.getResource(p.getReferredTo(), field.getType());

                }

                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(instance, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            return task;

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);

        }
    }

    static class Property implements Serializable {
        private final transient Field field;

        private ActionParameterType type;

        private String referredTo;

        private boolean required;

        private String description;

        Property(Field field) {
            this.field = field;
            ActionParameter parameter = field.getAnnotation(ActionParameter.class);
            if (parameter != null) {
                set(parameter.type(), parameter.referredTo(), parameter.required(), parameter.description());
            } else {
                set(ActionParameterType.INPUT, "", false, "");
            }
        }

        void set(ActionParameterType type,
                 String referredTo,
                 boolean required,
                 String description) {
            this.type = type;
            this.referredTo = referredTo;
            this.required = required;
            this.description = description;
        }

        public Field getField() {
            return field;
        }

        public ActionParameterType getType() {
            return type;
        }

        public String getReferredTo() {
            return referredTo;
        }

        public boolean isRequired() {
            return required;
        }

        public String getDescription() {
            return description;
        }
    }

}
