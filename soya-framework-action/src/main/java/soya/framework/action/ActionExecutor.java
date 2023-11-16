package soya.framework.action;

import soya.framework.action.util.ConvertUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class ActionExecutor<T> {

    private final ActionName actionName;

    private Callable<T> callable;
    private Map<String, ParameterSetter> parameters = new LinkedHashMap<>();

    private String inputParamName;

    ActionExecutor(ActionName actionName, Callable<T> callable) {
        this.actionName = actionName;
        this.callable = callable;
    }

    public ActionName getActionName() {
        return actionName;
    }

    public String[] getParameterNames() {
        return parameters.keySet().toArray(new String[parameters.size()]);
    }

    public String getInputParameterName() {
        return inputParamName;
    }

    public Class<?> getParameterType(String name) {
        return parameters.get(name).field.getType();
    }

    public boolean required(String name) {
        return parameters.get(name).required;
    }

    public String getReferredTo(String name) {
        return parameters.get(name).referredTo;
    }

    public ActionExecutor<T> set(String name, String value) {
        if (parameters.containsKey(name)) {
            parameters.get(name).setValue(value);
        } else {
            throw new IllegalArgumentException("Parameter '" + name + "' is not defined for action: " + actionName);
        }
        return this;
    }

    public T execute() throws Exception {
        populate();
        return callable.call();
    }

    public Future<T> submit(ExecutorService executorService) throws Exception {
        populate();
        return executorService.submit(callable);
    }

    private void populate() {
        parameters.values().forEach(e -> {
            Field field = e.field;
            field.setAccessible(true);
            try {
                field.set(callable, e.getValue());
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    void addParameter(Field field, String ref, boolean required, boolean input) {
        if(input && this.inputParamName == null) {
            this.inputParamName = field.getName();
        }
        parameters.put(field.getName(), new ParameterSetter(field, ref, required));
    }

    static class ParameterSetter {
        private final Field field;
        private final String referredTo;
        private final boolean required;

        private Object value;

        ParameterSetter(Field field, String referredTo, boolean required) {
            this.field = field;
            this.referredTo = referredTo;
            this.required = required;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = ConvertUtils.convert(value, field.getType());
        }
    }

}
