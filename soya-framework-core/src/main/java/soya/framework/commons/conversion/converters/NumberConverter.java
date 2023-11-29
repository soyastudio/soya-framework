package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

class NumberConverter implements Converter<Number> {
    NumberConverter() {
    }

    @Override
    public <D extends Number> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return fromString((String) value, destType);

        } else {
            return fromString(Objects.toString(value), destType);
        }
    }

    private <D extends Number> D fromString(String str, Class<D> destType) throws ConversionException {
        try {
            if (Byte.class == destType || Byte.TYPE == destType) {
                return (D) Byte.valueOf(str);

            } else if (Short.class == destType || Short.TYPE == destType) {
                return (D) Short.valueOf(str);

            } else if (Integer.class == destType || Integer.TYPE == destType) {
                return (D) Integer.valueOf(str);

            } else if (Long.class == destType || Long.TYPE == destType) {
                return (D) Long.valueOf(str);

            } else if (Float.class == destType || Float.TYPE == destType) {
                return (D) Float.valueOf(str);

            } else if (Double.class == destType || Double.TYPE == destType) {
                return (D) Double.valueOf(str);

            } else if (BigInteger.class == destType) {
                return (D) BigInteger.valueOf(Long.valueOf(str));

            } else if (BigDecimal.class == destType) {
                return (D) BigDecimal.valueOf(Double.valueOf(str));

            } else {
                throw new ConversionException(str, destType);
            }
        } catch (NumberFormatException e) {
            throw new ConversionException(e);
        }


    }
}
