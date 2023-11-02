package soya.framework.restruts.reflect;

import soya.framework.restruts.RestActionContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MethodInvoker {

    String className = null;
    String methodName = null;
    private Class<?>[] paramTypes = {};
    private Map<Integer, String> paramSettings = new HashMap<>();

    public MethodInvoker(String expression) {
        parse(expression);
    }

    public Object execute(RestActionContext context, String input) throws Exception {
        Object service = context.getDependencyInjector().getWiredResource(className);

        Method method = getMethod(service.getClass(), methodName, paramTypes);
        Class<?>[] argTypes = method.getParameterTypes();
        Object[] argValues = new Object[argTypes.length];
        for (int i = 0; i < argValues.length; i ++) {
            String setting = paramSettings.get(i);

            argValues[i] = setting;
        }

        return method.invoke(service, argValues);
    }

    private void parse(String exp) {
        String queryString = null;
        String parameterPart = null;

        String token = exp;
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

        if (parameterPart != null) {
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

    private Method getMethod(Class<?> cls, String name, Class<?>[] paramTypes) throws Exception {
        Method method = cls.getMethod(name, paramTypes);

        return method;
    }
}
