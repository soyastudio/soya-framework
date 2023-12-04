package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URIConverter implements Converter<URI> {
    URIConverter() {
    }

    @Override
    public <D extends URI> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value instanceof String) {
            try {
                return (D) new URI((String) value);

            } catch (URISyntaxException e) {
                throw new ConversionException(e);
            }

        } else if (value instanceof File) {
            return (D) ((File) value).toURI();

        } else if (value instanceof URL) {
            try {
                return (D) ((URL) value).toURI();

            } catch (URISyntaxException e) {
                throw new ConversionException(e);
            }
        } else {
            throw new ConversionException(value, destType);

        }
    }
}
