package soya.framework.commons.conversion;

public interface Converter<T> {
    <D extends T> D convert(Object value, Class<D> destType) throws ConversionException;
}
