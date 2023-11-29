package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;
import soya.framework.commons.io.Resource;
import soya.framework.commons.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

class StringConverter implements Converter<String> {
    StringConverter() {
    }

    @Override
    public <D extends String> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value instanceof byte[]) {
            return (D) new String((byte[]) value);

        } else if (value instanceof InputStream) {
            try {
                return (D) new String(StreamUtils.copyToByteArray((InputStream) value));

            } catch (IOException e) {
                throw new ConversionException(e);
            }

        } else if (value instanceof Resource) {
            return (D) ((Resource) value).getAsString(Charset.defaultCharset());

        } else {
            return (D) value.toString();

        }
    }
}
