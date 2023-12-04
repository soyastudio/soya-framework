package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;
import soya.framework.commons.io.Resource;
import soya.framework.context.ServiceLocatorSingleton;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceConverter implements Converter<Resource> {
    ResourceConverter() {
    }

    @Override
    public <D extends Resource> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value instanceof String) {
            try {
                return (D) ServiceLocatorSingleton.getInstance().getResource(new URI((String) value));
            } catch (URISyntaxException e) {
                throw new ConversionException(e);
            }
        } else if(value instanceof URI) {
            return (D) ServiceLocatorSingleton.getInstance().getResource((URI) value);

        } else if(value instanceof URL) {
            try {
                return (D) ServiceLocatorSingleton.getInstance().getResource(((URL)value).toURI());
            } catch (URISyntaxException e) {
                throw new ConversionException(e);
            }
        } else {
            throw new ConversionException(value, destType);

        }
    }
}
