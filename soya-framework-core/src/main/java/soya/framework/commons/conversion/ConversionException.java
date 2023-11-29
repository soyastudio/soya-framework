package soya.framework.commons.conversion;

public class ConversionException extends RuntimeException {

    public ConversionException(Object value, Class<?> type) {
        super("Cannot convert from value type '" + value.getClass().getName() + "' to dest type '" + type.getName() + "'.");
    }

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(Throwable cause) {
        super(cause);
    }
}
