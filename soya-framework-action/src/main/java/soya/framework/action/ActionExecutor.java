package soya.framework.action;

import soya.framework.action.util.ConvertUtils;
import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class ActionExecutor<T> {

    private final ActionName actionName;
    private Map<String, Property> properties = new HashMap<>();
    private String inputParamName;

    private Callable<?> callable;

    ActionExecutor(ActionName actionName, Callable<?> callable, ActionProperty[] parameters) {
        this.actionName = actionName;
        Arrays.stream(parameters).forEach(e -> {
            properties.put(e.getName(), new Property(e));
            if(e.getParameterType().equals(ActionParameterType.INPUT)) {
                inputParamName = e.getName();
            }
        });

        this.callable = callable;
    }

    public ActionName getActionName() {
        return actionName;
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
        return properties.get(name).parameter.getType();
    }

    public boolean isParameterRequired(String name) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("Parameter is not defined: " + name);
        }
        return properties.get(name).parameter.isRequired();
    }

    public void set(String name, String value) {
        if (!properties.containsKey(name)) {
            throw new IllegalArgumentException("Parameter is not defined: " + name);
        }

        Property property = properties.get(name);
        property.set(ConvertUtils.convert(value, property.parameter.getType()));
    }

    public ActionExecutor<T> reset() {
        properties.entrySet().forEach(e -> {
            e.getValue().reset();
        });
        return this;
    }

    public T execute() throws Exception {
        populate(callable);
        return (T) callable.call();
    }

    public Future<T> submit(ExecutorService executorService) throws Exception {
        populate(callable);
        return (Future<T>) executorService.submit(callable);
    }

    private void populate(Callable<?> callable) {
        Iterator<Property> iterator = properties.values().iterator();
        while (iterator.hasNext()) {
            Property property = iterator.next();
            if (property.isRequired() && property.value == null) {
                throw new IllegalStateException("Property is required: " + property.getName());
            }
        }

        Class<? extends Callable<?>> actionType = (Class<? extends Callable<?>>) callable.getClass();
        if(DynaAction.class.isAssignableFrom(actionType)) {
            DynaAction dynamicAction = (DynaAction) callable;
            properties.entrySet().forEach(e -> {
                Object value = properties.get(e.getKey()).value;
                if (value != null) {
                    dynamicAction.setParameter(e.getKey(), value);
                }
            });

        } else {
            properties.entrySet().forEach(e -> {
                Field field = ReflectUtils.findField(actionType, e.getKey());
                Object value = properties.get(field.getName()).value;
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        field.set(callable, value);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);

                    }
                }
            });
        }
    }


    static class Property {
        private ActionProperty parameter;

        private Object value;

        public Property(ActionProperty parameter) {
            this.parameter = parameter;

        }

        String getName() {
            return parameter.getName();
        }

        Class<?> getType() {
            return parameter.getType();
        }

        ActionParameterType getParameterType() {
            return parameter.getParameterType();
        }

        String getReferredTo() {
            return parameter.getReferredTo();
        }

        boolean isRequired() {
            return parameter.isRequired();
        }

        void set(Object value) {
            this.value = value;
        }

        void reset() {
            this.value = null;
        }
    }

}
