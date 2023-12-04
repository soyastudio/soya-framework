package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;

import java.util.Locale;

public class BooleanConverter implements Converter<Boolean> {
    private String[] trueStrings = {"true", "yes", "y", "on", "1"};

    private String[] falseStrings = {"false", "no", "n", "off", "0"};


    BooleanConverter() {
    }

    @Override
    public <D extends Boolean> D convert(Object value, Class<D> destType) throws ConversionException {
        if (Boolean.class.equals(destType) || Boolean.TYPE.equals(destType)) {
            final String stringValue = value.toString().toLowerCase(Locale.ROOT);

            for (final String trueString : trueStrings) {
                if (trueString.equals(stringValue)) {
                    return (D) Boolean.TRUE;
                }
            }

            for (final String falseString : falseStrings) {
                if (falseString.equals(stringValue)) {
                    return (D) Boolean.FALSE;
                }
            }
        }

        throw new ConversionException("Cannot convert '" + value + "' to Boolean.");
    }
}
