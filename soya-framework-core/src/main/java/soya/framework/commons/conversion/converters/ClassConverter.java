package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

public class ClassConverter implements Converter<Class> {
    ClassConverter() {
    }

    @Override
    public <D extends Class> D convert(Object value, Class<D> destType) throws ConversionException {
        try {
            return (D) Class.forName(value.toString());
        } catch (ClassNotFoundException e) {
            throw new ConversionException(e);
        }
    }
}
