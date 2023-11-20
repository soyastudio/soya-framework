package soya.framework.action;

import soya.framework.action.util.ConvertUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class ActionExecutor<T> {
    private final ActionClass actionClass;
    private final ActionContext actionContext;
    private List<String> wired = new ArrayList<>();
    private Map<String, Property> properties = new HashMap<>();
    private String inputParamName;

    ActionExecutor(ActionClass actionClass, ActionContext actionContext) {
        this.actionClass = actionClass;
        this.actionContext = actionContext;

        Arrays.stream(actionClass.parameters()).forEach(e -> {
            String paramName = e.getField().getName();
            ActionParameterType paramType = e.getType();
            if (paramType.isWired()) {
                wired.add(paramName);

            } else if (!paramType.isWired()) {
                properties.put(paramName, new Property(e));
                if (paramType.equals(ActionParameterType.INPUT)) {
                    if (inputParamName != null) {
                        throw new IllegalArgumentException("Action can only have one field annotated as INPUT parameter!");
                    } else {
                        inputParamName = paramName;
                    }
                }
            }
        });
    }

    public ActionName getActionName() {
        return actionClass.getActionName();
    }

    public String[] getParameterNames() {
        List<String> list = new ArrayList<>(properties.keySet());
        return list.toArray(new String[list.size()]);
    }

    public String getInputParameterName() {
        return inputParamName;
    }

    public Class<?> getParameterType(String name) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("Parameter is not defined: " + name);
        }
        return properties.get(name).type;
    }

    public boolean isParameterRequired(String name) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("Parameter is not defined: " + name);
        }
        return properties.get(name).required;
    }

    public void set(String name, String value) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("Parameter is not defined: " + name);
        }

        Property property = properties.get(name);
        property.set(ConvertUtils.convert(value, property.type));
    }

    public ActionExecutor<T> reset() {
        properties.entrySet().forEach(e -> {
            e.getValue().reset();
        });
        return this;
    }

    public T execute() throws Exception {
        return (T) create().call();
    }

    public Future<T> submit(ExecutorService executorService) throws Exception {
        return (Future<T>) executorService.submit(create());
    }

    private Callable<?> create() throws InstantiationException, IllegalAccessException {
        Iterator<Property> iterator = properties.values().iterator();
        while (iterator.hasNext()) {
            Property property = iterator.next();
            if (property.required && property.value == null) {
                throw new IllegalStateException("Property is required: " + property.name);
            }
        }

        Callable<?> callable = actionClass.getActionType().newInstance();
        Arrays.stream(actionClass.parameters()).forEach(parameter -> {
            Field field = parameter.getField();
            Object value = null;
            if (!parameter.getType().isWired()) {
                value = properties.get(field.getName()).value;

            } else if (ActionParameterType.WIRED_VALUE.equals(parameter.getType())) {
                value = ConvertUtils.convert(parameter.getReferredTo(), field.getType());

            } else if (ActionParameterType.WIRED_PROPERTY.equals(parameter.getType())) {
                value = ConvertUtils.convert(actionContext.getProperty(parameter.getReferredTo(), parameter.isRequired()), field.getType());

            } else if (ActionParameterType.WIRED_SERVICE.equals(parameter.getType())) {
                try {
                    // FIXME:
                    value = actionContext.getService(parameter.getReferredTo(), field.getType());
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (ActionParameterType.WIRED_RESOURCE.equals(parameter.getType())) {
                // FIXME:
                value = actionContext.getResource(parameter.getReferredTo(), field.getType());

            }

            if (value != null) {
                field.setAccessible(true);
                try {
                    field.set(callable, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        return callable;
    }

    static class Property {
        private final String name;
        private final Class<?> type;

        private final boolean required;

        private Object value;

        public Property(ActionClass.Parameter parameter) {
            this.name = parameter.getField().getName();
            this.type = parameter.getField().getType();
            this.required = parameter.isRequired();

        }

        void set(Object value) {
            this.value = value;
        }

        void reset() {
            this.value = null;
        }
    }

}
