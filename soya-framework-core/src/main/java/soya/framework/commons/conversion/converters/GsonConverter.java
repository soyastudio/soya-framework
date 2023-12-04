package soya.framework.commons.conversion.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

public class GsonConverter implements Converter<JsonElement> {
    private static Gson GSON = new Gson();

    GsonConverter() {
    }

    @Override
    public <D extends JsonElement> D convert(Object value, Class<D> destType) throws ConversionException {
        if(value instanceof String) {
            return (D) JsonParser.parseString((String)value);

        } else {
            return (D) GSON.toJsonTree(value);
        }
    }

    public static void configure(GsonBuilder builder) {
        GSON = builder.create();
    }
}
