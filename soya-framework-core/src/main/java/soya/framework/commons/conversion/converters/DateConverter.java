package soya.framework.commons.conversion.converters;

import soya.framework.commons.conversion.ConversionException;
import soya.framework.commons.conversion.Converter;
import soya.framework.commons.util.DefaultUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

class DateConverter implements Converter<Date> {

    DateConverter() {
    }

    @Override
    public <D extends Date> D convert(Object value, Class<D> destType) throws ConversionException {
        if (value instanceof String) {
            try {
                return (D) DefaultUtils.DEFAULT_DATE_FORMAT.parse((String) value);
            } catch (ParseException e) {
                throw new ConversionException(e);
            }

        } else if (value instanceof Long) {
            try {
                return destType.getConstructor(Long.class).newInstance((Long)value);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ConversionException(e);
            }

        } else if (value instanceof Calendar) {
            return (D) ((Calendar) value).getTime();

        } else {
            throw new ConversionException(value, destType);
        }
    }
}
