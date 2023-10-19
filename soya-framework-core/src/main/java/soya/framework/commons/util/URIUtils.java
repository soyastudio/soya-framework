package soya.framework.commons.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

public class URIUtils {

    private URIUtils() {
    }

    public static URI toURI(String commandline) {
        StringBuilder builder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(commandline);
        String uri = null;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (uri == null) {
                uri = token;
                builder.append(uri);

            } else if (token.startsWith("-")) {
                if (builder.length() == uri.length()) {
                    builder.append("?");
                } else {
                    builder.append("&");
                }

                if (token.startsWith("--")) {
                    builder.append(token.substring(2));
                } else {
                    builder.append(token.substring(1));
                }
                builder.append("=");

            } else {
                builder.append(token);
            }
        }

        return URI.create(builder.toString());

    }

    public static URI toURI(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("");
        }

        StringBuilder builder = new StringBuilder(args[0]);
        if (args.length > 1) {
            builder.append("?");
            for (int i = 1; i < args.length; i++) {
                if (args[i].startsWith("--")) {
                    builder.append(args[i].substring(2)).append("=");

                } else if (args[i].startsWith("-")) {
                    builder.append(args[i].substring(1)).append("=");

                } else {
                    builder.append(args[i]).append("&");

                }
            }
        }

        if (builder.charAt(builder.length() - 1) == '&') {
            builder.deleteCharAt(builder.length() - 1);
        }

        return URI.create(builder.toString());
    }

    public static Map<String, List<String>> splitQuery(String query) {
        Map<String, List<String>> params = new HashMap<>();
        try {
            params = splitQuery(query, "UTF-8");

        } catch (UnsupportedEncodingException e) {

        }
        return params;
    }

    public static Map<String, List<String>> splitQuery(String query, String encoding) throws
            UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        if (query != null && !query.isEmpty()) {
            final String[] pairs = query.split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), encoding) : pair;
                if (!query_pairs.containsKey(key)) {
                    query_pairs.put(key, new LinkedList<String>());
                }
                final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                query_pairs.get(key).add(value);
            }
        }
        return query_pairs;
    }

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
}
