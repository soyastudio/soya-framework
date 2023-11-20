package soya.framework.commons.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Base64Utils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Base64Utils() {
    }

    public static boolean isBase64(String s) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(s);
        return m.find();
    }

    public static byte[] encode(byte[] src) {
        Objects.requireNonNull(src);
        if (src.length == 0) {
            return src;
        }
        return Base64.getEncoder().encode(src);
    }

    public static byte[] decode(byte[] src) {
        Objects.requireNonNull(src);
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }

    public static byte[] encodeURL(byte[] src) {
        Objects.requireNonNull(src);
        if (src.length == 0) {
            return src;
        }
        return Base64.getUrlEncoder().encode(src);
    }

    public static byte[] decodeURL(byte[] src) {
        Objects.requireNonNull(src);
        if (src.length == 0) {
            return src;
        }
        return Base64.getUrlDecoder().decode(src);
    }

    public static String encodeToString(byte[] src) {
        Objects.requireNonNull(src);
        if (src.length == 0) {
            return "";
        }
        return new String(encode(src), DEFAULT_CHARSET);
    }

    public static byte[] decodeFromString(String src) {
        Objects.requireNonNull(src);
        if (src.isEmpty()) {
            return new byte[0];
        }
        return decode(src.getBytes(DEFAULT_CHARSET));
    }

    public static String encodeToUrlString(byte[] src) {
        Objects.requireNonNull(src);
        return new String(encodeURL(src), DEFAULT_CHARSET);
    }

    public static byte[] decodeFromUrlString(String src) {
        Objects.requireNonNull(src);
        return decodeURL(src.getBytes(DEFAULT_CHARSET));
    }

}
