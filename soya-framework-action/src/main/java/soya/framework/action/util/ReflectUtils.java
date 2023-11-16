package soya.framework.action.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ReflectUtils {
    private ReflectUtils() {
    }

    public static Field[] getFields(Class<?> cls) {
        if (cls.getSuperclass().equals(Object.class)) {
            return cls.getDeclaredFields();
        }

        Map<String, Field> fieldMap = new LinkedHashMap<>();
        Class<?> parent = cls;
        while (!parent.equals(Object.class)) {
            Arrays.stream(parent.getDeclaredFields()).forEach(e -> {
                if (!Modifier.isStatic(e.getModifiers())
                        && !Modifier.isFinal(e.getModifiers())
                        && !fieldMap.containsKey(e.getName())) {
                    fieldMap.put(e.getName(), e);

                }
            });
            parent = parent.getSuperclass();
        }
        return fieldMap.values().toArray(new Field[fieldMap.size()]);
    }

    public static Field getField(String name, Class<?> cls) {
        Field field = null;
        Class<?> clazz = cls;

        while(field == null) {
            if(clazz.equals(Object.class)) {
                break;
            }

            try {
                field = clazz.getDeclaredField(name);
                clazz = clazz.getSuperclass();

            } catch (NoSuchFieldException e) {

            }
        }

        if(field == null) {
            throw new RuntimeException(
                    new NoSuchFieldException("No such field '" + name + "' for class " + cls.getName()));
        }

        return field;
    }
}
