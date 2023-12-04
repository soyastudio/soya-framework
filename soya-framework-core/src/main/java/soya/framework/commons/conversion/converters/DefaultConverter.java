package soya.framework.commons.conversion.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;
import soya.framework.commons.io.Resource;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DefaultConverter implements Converter<Object> {

    private static Converter me;

    private Map<Class<?>, Converter> converters = new LinkedHashMap<>();
    private Converter<Object> defaultConverter = new ObjectConverter();

    static {
        DefaultConverter converter = new DefaultConverter()
                .register(String.class, new StringConverter())
                .register(Boolean.class, new BooleanConverter())
                .register(Class.class, new ClassConverter())
                .register(Date.class, new DateConverter())
                .register(File.class, new FileConverter())
                .register(JsonElement.class, new GsonConverter())
                .register(Number.class, new NumberConverter())
                .register(Resource.class, new ResourceConverter())
                .register(URI.class, new URIConverter())
                .register(URL.class, new URLConverter());

        me = converter;
    }

    private DefaultConverter() {
    }

    public static Converter getInstance() {
        return me;
    }

    public DefaultConverter resetDefault(Converter<Object> converter) {
        DefaultConverter defaultConverter = (DefaultConverter) me;
        defaultConverter.defaultConverter = converter;
        return defaultConverter;
    }

    public DefaultConverter resetDefault(Converter<Object> converter, Map<Class<?>, Converter> converters) {
        DefaultConverter defaultConverter = (DefaultConverter) me;
        defaultConverter.defaultConverter = converter;
        defaultConverter.converters.clear();
        defaultConverter.converters.putAll(converters);
        return defaultConverter;
    }

    public DefaultConverter register(Class<?> type, Converter converter) {
        converters.put(type, converter);
        return this;
    }

    public Converter<?> getConverter(Class<?> destType) {
        for (Map.Entry<Class<?>, Converter> entry : converters.entrySet()) {
            if (entry.getKey().isAssignableFrom(destType)) {
                return entry.getValue();
            }
        }
        return defaultConverter;
    }

    @Override
    public <D> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value == null) {
            return null;

        } else if (destType.isInstance(value)) {
            return (D) value;

        } else {
            Converter<D> converter= (Converter<D>) getConverter(destType);
            return converter.convert(value, destType);
        }
    }

    static class ObjectConverter implements Converter<Object> {

        private Gson gson = new GsonBuilder().setPrettyPrinting().create();

        private ObjectConverter() {
        }

        @Override
        public <D> D convert(Object value, Class<D> destType) throws ConversionException {

            if (value instanceof JsonElement) {
                return gson.fromJson((JsonElement) value, destType);

            } else {
                return gson.fromJson(value.toString(), destType);
            }
        }
    }

}
