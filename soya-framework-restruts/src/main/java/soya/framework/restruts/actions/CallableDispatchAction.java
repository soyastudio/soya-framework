package soya.framework.restruts.actions;

import soya.framework.restruts.DispatchAction;
import soya.framework.restruts.RestAction;
import soya.framework.restruts.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class CallableDispatchAction<T> extends DispatchAction<T> {
    private Class<? extends Callable> callableType;
    private List<Field> fieldList = new ArrayList<>();

    public CallableDispatchAction() {
        RestAction annotation = getClass().getAnnotation(RestAction.class);
        if (annotation == null) {
            throw new IllegalArgumentException("");
        }

        init(annotation.action());
    }

    private void init(String action) {
        try {
            callableType = (Class<? extends Callable>) Class.forName(action);
            Field[] fields = ReflectUtils.getFields(callableType);
            Arrays.stream(fields).forEach(e -> {
                if (!Modifier.isStatic(e.getModifiers()) && !Modifier.isFinal(e.getModifiers())) {
                    fieldList.add(e);
                    properties.put(e.getName(), new ServiceDispatchAction.DynaProperty(e.getName(), e.getType()));
                }
            });
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T call() throws Exception {
        Callable callable = callableType.newInstance();

        fieldList.forEach(e -> {
            e.setAccessible(true);
            try {
                e.set(callable, properties.get(e.getName()).getValue());
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        return (T) callable.call();
    }
}
