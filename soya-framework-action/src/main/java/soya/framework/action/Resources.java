package soya.framework.action;

import soya.framework.commons.util.StreamUtils;
import soya.framework.commons.util.URIUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class Resources {

    public static final String SCHEMA_ENVIRONMENT = "env";

    public static final String SCHEMA_CLASSPATH = "classpath";

    public static final String SCHEMA_BASE64 = "base64";

    public static final String SCHEMA_GZIP = "gzip";

    public static final String SCHEMA_ACTION = "action";

    private static Map<String, Class<? extends Resource>> resourceTypes;

    static {
        resourceTypes = new HashMap<>();
        resourceTypes.put(SCHEMA_BASE64, Base64EncodedResource.class);
        resourceTypes.put(SCHEMA_GZIP, GZipEncodedResource.class);
        resourceTypes.put(SCHEMA_CLASSPATH, ClasspathResource.class);
        resourceTypes.put(SCHEMA_ENVIRONMENT, EnvironmentResource.class);
        resourceTypes.put(SCHEMA_ACTION, ActionResource.class);
    }

    public static void register(String schema, Class<? extends Resource> resourceType) {
        resourceTypes.put(schema, resourceType);
    }

    public static String getResourceAsString(String uri) throws ResourceException {
        try {
            return get(uri).getAsString(Charset.defaultCharset());

        } catch (ResourceException e) {
            throw new ResourceException(e);
        }
    }

    public static InputStream getResourceAsInputStream(String uri) throws ResourceException {
        try {
            return get(uri).getAsInputStream();

        } catch (ResourceException e) {
            throw new ResourceException(e);
        }
    }

    private static Resource get(String uri) {
        URI u = URI.create(uri);

        String schema = u.getScheme();
        Class<? extends Resource> resourceType = URLResource.class;
        if (resourceTypes.containsKey(schema)) {
            resourceType = resourceTypes.get(schema);
        }

        try {
            Constructor constructor = resourceType.getConstructor(String.class);
            constructor.setAccessible(true);
            return (Resource) constructor.newInstance(new Object[]{uri});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static abstract class AbstractResource implements Resource {
        protected URI uri;

        public AbstractResource(String uri) {
            this.uri = URI.create(uri);
        }
    }

    static class EnvironmentResource extends AbstractResource {

        public EnvironmentResource(String uri) {
            super(uri);
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            try {
                return ActionContext.getInstance().getProperty(uri.getHost());

            } catch (Exception e) {
                return System.getProperty(uri.getHost());
            }
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            try {
                return new ByteArrayInputStream(getAsString(Charset.defaultCharset()).getBytes());

            } catch (NullPointerException e) {
                return null;
            }
        }
    }

    static class ClasspathResource extends AbstractResource {

        public ClasspathResource(String uri) {
            super(uri);
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            try {
                return new String(StreamUtils.copyToByteArray(getAsInputStream()), encoding);

            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            String res = uri.getPath() != null ? uri.getHost() + uri.getPath() : uri.getHost();
            return getClassLoader().getResourceAsStream(res);
        }

        private ClassLoader getClassLoader() {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = getClass().getClassLoader();
            }

            return loader;
        }
    }

    static class GZipEncodedResource extends AbstractResource {

        private String data;

        public GZipEncodedResource(String uri) {
            super(uri);
            this.data = uri.substring("gzip://".length());
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            byte[] compressed = Base64.getDecoder().decode(data);
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed)) {
                try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
                    try (InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, encoding)) {
                        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                            StringBuilder output = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                output.append(line);
                            }
                            return output.toString();
                        }
                    }
                }
            } catch (Exception e) {
                throw new ResourceException(e);
            }
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            byte[] compressed = Base64.getDecoder().decode(data);
            try {
                return new GZIPInputStream(new ByteArrayInputStream(compressed));
            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }
    }

    static class Base64EncodedResource extends AbstractResource {

        private String data;

        public Base64EncodedResource(String uri) {
            super(uri);
            this.data = uri.substring("base64://".length());
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            return new String(getAsByteArray(), encoding);
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            return new ByteArrayInputStream(getAsByteArray());
        }

        private byte[] getAsByteArray() {
            return Base64.getDecoder().decode(data);
        }
    }

    static class URLResource extends AbstractResource {
        public URLResource(String uri) {
            super(uri);
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            try {
                byte[] data = StreamUtils.copyToByteArray(getAsInputStream());
                return new String(data, encoding);
            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            try {
                return uri.toURL().openStream();
            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }
    }

    static class ActionResource extends AbstractResource {

        private ActionClass actionClass;
        private Map<String, List<String>> params = new HashMap<>();

        public ActionResource(String uri) {
            super(uri);
            ActionName actionName = ActionName.create(this.uri.getHost(), this.uri.getPath().substring(1).replaceAll("/", "."));

            if (actionName.getDomain().equals("class")) {
                try {
                    this.actionClass = ActionClass.get((Class<? extends ActionCallable>) Class.forName(actionName.getName()));
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                this.actionClass = ActionClass.get(actionName);
            }

            if (this.uri.getQuery() != null) {
                params = URIUtils.splitQuery(this.uri.getQuery());
            }
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            Object result = extract();
            if (result instanceof String) {
                return (String) result;
            }

            return null;
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            return null;
        }

        private Object extract() {
            ActionExecutor executor = ActionExecutor.executor(actionClass.getActionType());
            params.entrySet().forEach(e -> {
                executor.setProperty(e.getKey(), e.getValue());
            });

            try {
                return executor.execute();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
