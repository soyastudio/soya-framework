package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class URLConverter implements Converter<URL> {
    URLConverter() {
    }

    @Override
    public <D extends URL> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value instanceof String) {
            try {
                return (D) new URL((String) value);
            } catch (MalformedURLException e) {
                throw new ConversionException(e);
            }

        } else if (value instanceof File) {
            try {
                return (D) ((File) value).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new ConversionException(e);
            }
        } else if (value instanceof URI) {
            try {
                return (D) ((URI) value).toURL();
            } catch (MalformedURLException e) {
                throw new ConversionException(e);
            }
        } else {
            throw new ConversionException(value, destType);

        }
    }
}
