package soya.framework.restruts.actions;

import soya.framework.restruts.DispatchAction;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.util.ConvertUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public abstract class ServiceDispatchAction<T> extends DispatchAction<T> {

    private ServiceInvoker invoker;

    public ServiceDispatchAction() {
        super();
        this.invoker = new ServiceInvoker(getAction());
        for(int i = 0; i < invoker.paramTypes.length; i ++) {
            String propName = invoker.paramSettings.get(i);
            if(propName == null) {
                throw new IllegalArgumentException();
            }
            properties.put(propName, new DynaProperty(propName, invoker.paramTypes[i]));
        }
    }

    protected String getAction() {
        RestAction annotation = getClass().getAnnotation(RestAction.class);
        if(annotation == null) {
            throw new IllegalArgumentException("");
        }

        return annotation.action();
    }

    @Override
    public T call() throws Exception {
        Object instance = getRestActionContext().getService(invoker.className);
        Method method = instance.getClass().getMethod(invoker.methodName, invoker.paramTypes);

        properties.entrySet().forEach(e -> {
            try {
                Field field = getClass().getDeclaredField(e.getKey());
                field.setAccessible(true);
                e.getValue().setValue(field.get(this));
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        return (T) method.invoke(instance, getArguments());
    }

    private Object[] getArguments() {
        List<Object> list = new ArrayList<>();
        properties.entrySet().forEach(e -> {
            list.add(e.getValue().getValue());
        });
        return list.toArray(new Object[list.size()]);
    }

    static class ServiceInvoker {
        private String className;
        private String methodName;
        private Class<?>[] paramTypes = new Class[0];
        private Map<Integer, String> paramSettings = new HashMap<>();

        ServiceInvoker(String action) {
            String queryString = null;
            String parameterPart = null;

            String token = action;
            int mark = token.indexOf('?');
            if (mark > 0) {
                queryString = token.substring(mark + 1);
                token = token.substring(0, mark);
            }

            mark = token.indexOf('(');
            if (mark > 0 && token.endsWith(")")) {
                parameterPart = token.substring(mark + 1, token.length() - 1);
                token = token.substring(0, mark);
            }

            token = token.replace("/", ".");

            mark = token.lastIndexOf('.');
            if (mark > 0) {
                methodName = token.substring(mark + 1);
                className = token.substring(0, mark);
            }

            if (parameterPart != null && parameterPart.trim().length() > 0) {
                String[] params = parameterPart.split(",");
                paramTypes = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    try {
                        String className = params[i].trim();
                        if (!className.contains(".")) {
                            className = "java.lang." + className;
                        }
                        paramTypes[i] = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (queryString != null) {
                String[] settings = queryString.split("&");
                Arrays.stream(settings).forEach(e -> {
                    if (e.contains("=")) {
                        int index = e.indexOf("=");
                        String key = e.substring(0, index).trim();
                        String value = e.substring(index + 1).trim();
                        try {
                            Integer paramIndex = Integer.parseInt(key);
                            paramSettings.put(paramIndex, value);
                        } catch (Exception ex) {

                        }
                    }
                });
            }
        }

    }
}
