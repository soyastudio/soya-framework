package soya.framework.commons.conversion.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DefaultConverter implements Converter<Object> {

    private static Converter me;

    private Map<Class<?>, Converter> converters = new LinkedHashMap<>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        me = new DefaultConverter();
    }

    private DefaultConverter() {
        register(String.class, new StringConverter());
        register(Boolean.class, new BooleanConverter());
        register(Class.class, new ClassConverter());
        register(Date.class, new DateConverter());
        register(Number.class, new NumberConverter());

    }

    public static Converter getInstance() {
        return me;
    }

    public void register(Class<?> type, Converter converter) {
        converters.put(type, converter);
    }

    @Override
    public <D> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value == null) {
            return null;

        } else if (destType.isInstance(value)) {
            return (D) value;

        } else {
            for (Map.Entry<Class<?>, Converter> entry : converters.entrySet()) {
                if (entry.getKey().isAssignableFrom(destType)) {
                    Converter converter = entry.getValue();
                    return (D) entry.getValue().convert(value, destType);
                }
            }

            if (value instanceof JsonElement) {
                return gson.fromJson((JsonElement) value, destType);

            } else {
                return gson.fromJson(value.toString(), destType);
            }
        }
    }
}
