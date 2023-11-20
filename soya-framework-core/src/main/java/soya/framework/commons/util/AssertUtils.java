package soya.framework.commons.util;


import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AssertUtils {

    // --------------------- check argument:
    public static <T> T argumentNonNull(T t) {
        if (t != null) {
            return t;
        }
        throw new IllegalArgumentException();
    }

    public static <T> T argumentNonNull(T t, String message) {
        if (t != null) {
            return t;
        }
        throw new IllegalArgumentException(message);
    }

    public static <T> T argumentNonNull(T t, T defaultValue) {
        if (t != null) {
            return t;
        } else {
            Objects.requireNonNull(defaultValue, "Default value is null.");
            return defaultValue;

        }
    }

    public static <T> T argumentNonEmpty(T t) {
        if (isNullOrEmpty(t)) {
            throw new IllegalArgumentException();
        }
        return t;
    }

    public static <T> T argumentNonEmpty(T t, String message) {
        if (isNullOrEmpty(t)) {
            throw new IllegalArgumentException(message);
        }
        return t;
    }

    public static <T> T argumentNonEmpty(T t, Supplier<T> supplier) {
        if (isNullOrEmpty(t)) {
            return supplier.get();
        }
        return t;
    }

    // --------------------- check state:
    public static void checkState(boolean expression) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, String message) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    // --------------------- requireNonNull:
    public static <T> T requireNonNull(T t) {
        return Objects.requireNonNull(t);
    }

    public static <T> T requireNonNull(T t, String message) {
        return Objects.requireNonNull(t, message);
    }

    public static <T> T requireNonNull(T t, Supplier<T> supplier) {
        if (t == null) {
            return supplier.get();
        }
        return t;
    }

    // --------------------- requireNonEmpty:
    public static <T> T requireNonEmpty(T t) {
        if (isNullOrEmpty(t)) {
            throw new IllegalStateException();
        }
        return t;
    }

    public static <T> T requireNonEmpty(T t, String message) {
        if (isNullOrEmpty(t)) {
            throw new IllegalStateException(message);
        }
        return t;
    }

    public static <T> T requireNonEmpty(T t, Supplier<T> supplier) {
        if (isNullOrEmpty(t)) {
            supplier.get();
        }
        return t;
    }

    // --------------------- empty check:
    public static boolean isNullOrEmpty(String st) {
        return st == null || st.trim().length() == 0;
    }

    public static <T> boolean isNullOrEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }

        if(obj instanceof String) {
            return ((String)obj).isEmpty();
        }

        // check isEmpty method
        try {
            Method method = obj.getClass().getMethod("isEmpty", new Class[0]);
            if (Modifier.isPublic(method.getModifiers()) && Boolean.class.isAssignableFrom(method.getReturnType())) {
                return (boolean) method.invoke(obj, new Object[0]);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // do nothing;
        }

        return false;
    }

    // --------------------- type check:
    public static <T> T cast(Object o, Class<T> type) {
        requireNonNull(o);
        return type.cast(type);
    }

    public static <T> T asType(Object o, Class<T> type) {
        requireNonNull(o);
        if (type.isInstance(o)) {
            return (T) o;
        }

        throw classCastException(o, type);
    }

    public static <T> T asType(Object o, Class<T> type, String message) {
        if (type.isInstance(o)) {
            return (T) o;
        }

        throw new ClassCastException(message);
    }

    // --------------------- generic asserts:
    public static <T> T asserts(T t, Predicate<T> predicate, Throwable e) {
        if (predicate.test(t)) {
            return t;
        }
        throw new AssertException(e);
    }

    public static <T> T asserts(T t, Predicate<T> predicate) throws AssertException {
        if (predicate.test(t)) {
            return t;
        }

        throw new AssertException();
    }

    public static <T> T asserts(T t, Predicate<T> predicate, Supplier<T> supplier) {
        if (predicate.test(t)) {
            return t;
        } else {
            return supplier.get();
        }
    }

    public static <T> T asserts(T t, boolean expression) {
        if (expression) {
            return t;
        }

        throw new AssertException();
    }

    public static <T> T asserts(T t, boolean expression, Throwable e) {
        if (expression) {
            return t;
        }

        throw new AssertException(e);
    }

    // --------------------- generic asserts:
    private static IllegalArgumentException illegalArgumentException(String message) {
        return message == null ? new IllegalArgumentException() : new IllegalArgumentException(message);
    }

    private static ClassCastException classCastException(Object o, Class<?> t) {
        return new ClassCastException("Cannot cast " + o.getClass().getName() + " to " + t.getName());
    }

    public static class AssertException extends RuntimeException {
        public AssertException() {
        }

        public AssertException(String message) {
            super(message);
        }

        public AssertException(String message, Throwable cause) {
            super(message, cause);
        }

        public AssertException(Throwable cause) {
            super(cause);
        }
    }

}
