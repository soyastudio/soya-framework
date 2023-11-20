package soya.framework.action;

import soya.framework.commons.util.ReflectUtils;

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

    private Map<String, Parameter> params = new LinkedHashMap<>();

    private ActionClass(Class<? extends Callable> actionType) {
        ActionDefinition annotation = actionType.getAnnotation(ActionDefinition.class);
        if (annotation == null) {
            throw new IllegalArgumentException();
        }
        this.actionName = new ActionName(annotation.domain(), annotation.name());
        this.actionType = actionType;

        // From ActionDefinition annotation:
        Arrays.stream(annotation.parameters()).forEach(e -> {
            if (params.containsKey(e.name())) {
                throw new IllegalArgumentException("");
            }

            Field field = ReflectUtils.getField(e.name(), actionType);
            Parameter property = new Parameter(field, e);
            params.put(e.name(), property);
        });

        Field[] fields = ReflectUtils.getFields(actionType);
        Arrays.stream(fields).forEach(field -> {
            if (!params.containsKey(field.getName())
                    && !Modifier.isFinal(field.getModifiers())
                    && !Modifier.isStatic(field.getModifiers())
                    && field.getAnnotation(ActionParameter.class) != null) {
                params.put(field.getName(), new Parameter(field));
            }
        });
    }

    public ActionName getActionName() {
        return actionName;
    }

    public Class<? extends Callable> getActionType() {
        return actionType;
    }

    Parameter[] parameters() {
        return params.values().toArray(new Parameter[params.size()]);
    }

    Parameter getParameter(String name) {
        return params.get(name);
    }

    public static ActionName register(Class<? extends Callable> cls) {
        if (cls.getAnnotation(ActionDefinition.class) == null) {
            throw new IllegalArgumentException("Class is not annotated as 'ActionDefinition': " + cls.getName());
        }

        ActionDefinition annotation = cls.getAnnotation(ActionDefinition.class);
        ActionName actionName = ActionName.create(annotation.domain(), annotation.name());
        if (!registrations.containsKey(actionName)) {
            registrations.put(actionName, new ActionClass(cls));
            return actionName;
        }
        return null;
    }

    public static ActionClass forName(ActionName actionName) {
        if (!registrations.containsKey(actionName)) {
            throw new IllegalArgumentException("Action does not exist: " + actionName);
        }

        return registrations.get(actionName);
    }

    public ActionExecutor<?> newInstance(ActionContext actionContext) {
        return new ActionExecutor<>(this, actionContext);

/*
        try {
            Callable<?> instance = registrations.get(actionName).actionType.newInstance();
            ActionExecutor<?> task = new ActionExecutor<>(actionName, instance);

            params.values().forEach(p -> {
                Field field = p.getField();
                Object value = null;
                if (p.type.equals(ActionParameterType.INPUT)) {
                    task.addParameter(field, p.referredTo, p.required, true);

                } else if (p.type.equals(ActionParameterType.PROPERTY)) {
                    task.addParameter(field, p.referredTo, p.required, false);

                } else if (p.type.equals(ActionParameterType.WIRED_SERVICE)) {
                    try {
                        if (p.getReferredTo().trim().isEmpty()) {
                            value = actionContext.getService(null, field.getType());
                        } else {
                            value = actionContext.getService(p.getReferredTo(), field.getType());
                        }

                    } catch (ServiceNotFoundException ex) {
                        if (p.isRequired()) {
                            throw new RuntimeException(ex);
                        }

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

        }*/
    }

    static class Parameter implements Serializable {
        private final transient Field field;

        private ActionParameterType type;
        private String referredTo;
        private boolean required;
        private String description;

        Parameter(Field field, ActionParameter annotation) {
            this.field = field;
            set(annotation.type(),
                    annotation.referredTo(),
                    annotation.required(),
                    annotation.description());
        }

        Parameter(Field field) {
            this.field = field;
            ActionParameter annotation = field.getAnnotation(ActionParameter.class);
            set(annotation.type(),
                    annotation.referredTo(),
                    annotation.required(),
                    annotation.description());
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
