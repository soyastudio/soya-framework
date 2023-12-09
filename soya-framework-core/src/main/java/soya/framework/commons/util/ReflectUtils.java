package soya.framework.commons.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReflectUtils {

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

    public static Field findField(Class<?> cls, String fieldName) {
        Class<?> parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            try {
                Field field = parent.getDeclaredField(fieldName);
                if (field != null) {
                    return field;
                }
            } catch (NoSuchFieldException e) {

            }
            parent = parent.getSuperclass();
        }

        throw new IllegalArgumentException("Cannot find field '" + fieldName + "' for class: " + cls.getName());
    }

    public static Method[] findMethods(Class<?> cls, String methodName) {
        List<Method> methodList = new ArrayList<>();
        Class parent = cls;
        while (parent != null) {
            for (Method method : parent.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isPublic(method.getModifiers())) {
                    methodList.add(method);
                }
            }

            parent = cls.getSuperclass();
        }

        return methodList.toArray(new Method[methodList.size()]);
    }

    public static Method findMethod(Class<?> cls, String methodName) {
        Class parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            List<Method> methodList = new ArrayList<>();
            for (Method method : parent.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isPublic(method.getModifiers())) {
                    methodList.add(method);
                }
            }

            if (methodList.size() == 1) {
                return methodList.get(0);
            } else if (methodList.size() > 1) {
                throw new IllegalArgumentException("There are " + methodList.size() + " methods named as '" + methodName + "' for class: " + cls.getName());
            }

            parent = cls.getSuperclass();
        }

        return null;
    }

    public static Method findMethod(Class<?> cls, String methodName, Class<?>[] parameterTypes) {
        Class parent = cls;
        while (!parent.getName().equals("java.lang.Object")) {
            try {
                return parent.getDeclaredMethod(methodName, parameterTypes);

            } catch (NoSuchMethodException e) {
                parent = cls.getSuperclass();
            }
        }

        return null;
    }


}
