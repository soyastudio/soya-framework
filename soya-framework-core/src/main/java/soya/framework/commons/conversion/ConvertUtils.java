package soya.framework.commons.conversion;

import soya.framework.commons.conversion.converters.DefaultConverter;

public final class ConvertUtils {
    private static Converter converter;

    private ConvertUtils() {
    }

    static {
        converter = DefaultConverter.getInstance();
    }

    public static Object convert(Object value, Class<?> type) {
        return converter.convert(value, type);
    }

}
