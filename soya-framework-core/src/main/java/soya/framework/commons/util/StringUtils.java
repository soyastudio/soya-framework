package soya.framework.commons.util;

import java.io.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StringUtils {
    private static String DEFAULT_ENCODING = "utf-8";

    public static String[] trim(String[] array) {
        if (array == null) {
            return null;
        }

        String[] arr = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                arr[i] = array[i].trim();

            } else {
                arr[i] = array[i];
            }
        }

        return arr;
    }

    public static String merge(String[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i > 0 && separator != null) {
                    builder.append(separator);
                }
                builder.append(array[i]);
            }

        }

        return builder.toString();

    }

    public static String base64Encode(String text) {
        return base64Encode(text, DEFAULT_ENCODING);
    }

    public static String base64Encode(String text, String encoding) {
        try {
            return Base64.getEncoder().encodeToString(text.getBytes(encoding));

        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String base64Decode(String text) {
        return base64Decode(text, DEFAULT_ENCODING);
    }

    public static String base64Decode(String text, String encoding) {
        try {
            return new String(Base64.getDecoder().decode(text.getBytes()), encoding);

        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isBase64(String s) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(s);
        return m.find();
    }

    public static String gzip(String text) {
        return gzip(text, DEFAULT_ENCODING);
    }

    public static String gzip(String text, String encoding) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(text.getBytes(encoding));
            }

            byte[] encoded = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
            return new String(encoded);

        } catch (IOException e) {
            throw new RuntimeException("Failed to zip content", e);
        }
    }

    public static String unzip(String text) {
        return unzip(text, DEFAULT_ENCODING);
    }

    public static String unzip(String text, String encoding) {
        byte[] encoded;
        try {
            encoded = text.getBytes(encoding);

        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }

        byte[] compressed = Base64.getDecoder().decode(encoded);

        if ((compressed == null) || (compressed.length == 0)) {
            throw new IllegalArgumentException("Cannot unzip null or empty bytes");
        }

        if (!isZipped(compressed)) {
            return new String(compressed);
        }

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed)) {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, encoding)) {
                    try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        StringBuilder output = new StringBuilder();
                        String line;
                        boolean boo = false;
                        while ((line = bufferedReader.readLine()) != null) {
                            if (boo) {
                                output.append("\n");
                            } else {
                                boo = true;
                            }

                            output.append(line);
                        }

                        return output.toString();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to unzip content", e);
        }
    }

    public static boolean isZipped(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
                && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
}
