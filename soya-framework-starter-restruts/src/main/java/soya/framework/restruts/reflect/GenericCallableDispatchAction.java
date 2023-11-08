package soya.framework.restruts.reflect;

import soya.framework.restruts.*;
import soya.framework.restruts.util.ConvertUtils;
import soya.framework.restruts.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@RestAction(
        id = "dispatch",
        path = "/restruts/dispatch/callable",
        method = HttpMethod.POST,
        parameters = {
                @RestActionParameter(name = "url", paramType = ParamType.HEADER_PARAM),
                @RestActionParameter(name = "input", paramType = ParamType.PAYLOAD)
        },
        produces = "text/plain",
        tags = "Restruct"
)

public class GenericCallableDispatchAction extends ReflectAction {

    private String url;

    private String input;

    @Override
    public String call() throws Exception {

        RestActionContext context = getRestActionContext();
        Invoker invoker = new Invoker(url);
        Callable<?> callable = invoker.callable;
        invoker.params.entrySet().forEach(e -> {
            Field field = e.getValue().field;
            String setting = e.getValue().setting;
            if (setting != null) {
                Object value = null;
                if (setting.equals("{}")) {
                    value = input;

                } else if (setting.startsWith("{") && setting.endsWith("}")) {
                    String ref = setting.substring(1, setting.length() - 1);
                    if (ref.contains(":")) {
                        value = context.getResource(ref, field.getType());

                    } else if (context.getProperty(ref) != null) {
                        value = ConvertUtils.convert(context.getProperty(ref), field.getType());

                    } else {
                        value = context.getService(ref, field.getType());
                    }
                } else {
                    value = ConvertUtils.convert(setting, field.getType());
                }

                try {
                    field.setAccessible(true);
                    field.set(callable, value);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Object result = callable.call();

        if (result == null) {
            return null;

        } else if (result instanceof String) {
            return (String) result;

        } else {
            return GSON.toJson(result);

        }
    }

    static class Invoker {
        private Callable<?> callable;
        private Map<String, FieldSetting> params = new LinkedHashMap<>();

        Invoker(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
            int qm = url.indexOf('?');
            String className = qm > 0 ? url.substring(0, qm) : url;
            String queryString = qm > 0 ? url.substring(qm + 1) : null;

            Class<?> cls = Class.forName(className);
            Method method = cls.getMethod("call", new Class[0]);
            System.out.println("------------------- return type: " + method.getReturnType());

            Field[] fields = ReflectUtils.getFields(cls);
            Arrays.stream(fields).forEach(e -> {
                if (!Modifier.isStatic(e.getModifiers()) && !Modifier.isFinal(e.getModifiers())) {
                    params.put(e.getName(), new FieldSetting(e));
                }
            });

            if (queryString != null) {
                String[] settings = queryString.split("&");
                Arrays.stream(settings).forEach(e -> {
                    if (e.contains("=")) {
                        int index = e.indexOf("=");
                        String key = e.substring(0, index).trim();
                        String value = e.substring(index + 1).trim();
                        if (params.containsKey(key)) {
                            params.get(key).set(value);
                        }
                    }
                });
            }

            this.callable = (Callable<?>) cls.newInstance();

        }
    }

    private static class FieldSetting {
        public FieldSetting(Field field) {
            this.field = field;
        }

        private final Field field;
        private String setting;

        public void set(String value) {
            this.setting = value;
        }

    }
}
