package soya.framework.commons.conversion.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BooleanConverterTest {

    public static final String[] STANDARD_TRUES = { "yes", "y", "true", "on", "1" };

    public static final String[] STANDARD_FALSES = { "no", "n", "false", "off", "0" };


    public static BooleanConverter converter = new BooleanConverter();


    @Test
    public void testConvertStringToBoolean() {
        Arrays.stream(STANDARD_TRUES).forEach(e -> {
            Assertions.assertEquals(converter.convert(e, Boolean.class), Boolean.TRUE);
        });
    }
}
