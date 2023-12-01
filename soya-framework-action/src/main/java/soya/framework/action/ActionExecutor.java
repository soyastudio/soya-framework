package soya.framework.action;

import soya.framework.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public final class ActionExecutor<T> {

    private static Logger logger = Logger.getLogger(ActionExecutor.class.getName());

    private final ActionName actionName;
    private Map<String, ActionParameter> parameters = new HashMap<>();
    private Callable<?> callable;

    ActionExecutor(ActionName actionName, Callable<?> callable, ActionProperty[] properties) {
        this.actionName = actionName;
        this.callable = callable;

        Arrays.stream(properties).forEach(e -> {
            parameters.put(e.getName(), new ActionParameter(e));
        });

    }

    public ActionName getActionName() {
        return actionName;
    }

    public void set(String name, String value) {
        if (!parameters.containsKey(name)) {
            throw new IllegalArgumentException("Parameter is not defined of visible: " + name);
        }

        parameters.get(name).set(value);
    }

    public ActionExecutor<T> reset() {
        parameters.entrySet().forEach(e -> {
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
        Iterator<ActionParameter> iterator = parameters.values().iterator();
        while (iterator.hasNext()) {
            ActionParameter parameter = iterator.next();
            if (parameter.isRequired() && parameter.get() == null) {
                throw new IllegalStateException("Property is required: " + parameter.getName());
            }
        }

        Class<? extends Callable<?>> actionType = (Class<? extends Callable<?>>) callable.getClass();
        if (DynaAction.class.isAssignableFrom(actionType)) {
            DynaAction dynamicAction = (DynaAction) callable;
            parameters.entrySet().forEach(e -> {
                Object value = parameters.get(e.getKey()).get();
                if (value != null) {
                    dynamicAction.setParameter(e.getKey(), value);
                }
            });

        } else {
            parameters.entrySet().forEach(e -> {
                Field field = ReflectUtils.findField(actionType, e.getKey());
                Object value = parameters.get(field.getName()).get();
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
}
