package soya.framework.commons.util;

import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DefaultUtils {
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat();

    private static final String DEFAULT_TYPE_NAME = DEFAULT_TYPE.class.getName();
    private static Map<Class<?>, Class<?>> DEFAULT_TYPES = new HashMap<>();

    private DefaultUtils() {
    }

    public static boolean isDefaultType(Class<?> cls) {
        return cls.getName().equals(DEFAULT_TYPE_NAME);
    }

    public static <T> Class<? extends T> getDefaultType(Class<T> type) {
        if(type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            if(DEFAULT_TYPES.containsKey(type)) {
                return (Class<? extends T>) DEFAULT_TYPES.get(type);

            } else {
                throw new RuntimeException("Cannot find default type for " + type.getName());
            }

        } else {
            return type;
        }
    }

    public static <T> T getDefaultValue(Class<T> type) {
        throw new RuntimeException("TODO:");
    }

    public static final class DEFAULT_TYPE {
    }

}
