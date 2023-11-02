package soya.framework.restruts;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DispatchAction<T> extends Action<T> {

    protected Method method;
    protected Object instance;
    protected Map<String, DynaProperty> properties = new LinkedHashMap<>();

    public DispatchAction() {
        super();
    }

    @Override
    public T call() throws Exception {
        init();
        //return (T) method.invoke(instance, getArguments());
        return (T) "!!!";
    }

    public String[] getPropertyNames() {
        return properties.keySet().toArray(new String[properties.size()]);
    }

    public Class<?> getPropertyType(String name) {
        if(!properties.containsKey(name)) {
            throw new IllegalArgumentException("Property not defined: " + name);
        }
        return properties.get(name).getType();
    }

    public void setProperty(String name, Object value) {

    }

    protected void init() {
        RestAction annotation = getClass().getAnnotation(RestAction.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Class is not annotated as 'RestAction': " + getClass().getName());

        } else if (annotation.action().isEmpty()) {
            throw new IllegalArgumentException("'action is not defined: " + getClass().getName());
        }

        String action = annotation.action();
        if (action.startsWith("bean:")) {
            String name = action.substring("bean:".length());
            if (name.startsWith("//")) {
                name = name.substring(2);
                if (name.contains("/")) {
                    int index = name.indexOf("/");
                    name = name.substring(0, index);
                    System.out.println("================= name: " + name);

                    this.instance = this.getWireService(name);

                    System.out.println("================= instance: " + instance);

                }
            }
        }

        this.method = getMethod(annotation);


        Parameter[] parameters = method.getParameters();
        RestActionParameter[] params = annotation.parameters();
        if (parameters.length != params.length) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < parameters.length; i++) {
            DynaProperty property = new DynaProperty(params[i].name(), parameters[i].getType());
            properties.put(property.getName(), property);
        }
    }

    protected Method getMethod(RestAction annotation) {
        return null;
    }

    protected Object[] getArguments() {
        List<Object> list = new ArrayList<>();
        properties.entrySet().forEach(e -> {
            list.add(e.getValue());
        });
        return list.toArray(new Object[list.size()]);
    }

    static class DynaProperty {
        private final String name;
        private final Class<?> type;

        private Object value;

        public DynaProperty(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
