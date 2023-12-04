package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class FileConverter implements Converter<File> {
    FileConverter() {
    }

    @Override
    public <D extends File> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value instanceof String) {
            return (D) new File((String) value);

        } else if (value instanceof URI) {
            try {
                return (D) new File(((URI) value).toURL().getFile());

            } catch (MalformedURLException e) {
                throw new ConversionException(e);
            }

        } else if (value instanceof URL) {
            return (D) new File(((URL) value).getFile());

        } else {
            throw new ConversionException(value, destType);

        }
    }
}
