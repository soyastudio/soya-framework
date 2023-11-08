package soya.framework.restruts.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.NamespaceResourceLoader;
import soya.framework.restruts.Resource;
import soya.framework.restruts.util.ConvertUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ClasspathResourceLoader implements NamespaceResourceLoader {

    private static final String CLASSPATH = "classpath:";
    public static final String[] NAMESPACES = {CLASSPATH};

    @Override
    public String[] getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public String getResource(String resource) {
        try {
            String path = resource.substring(CLASSPATH.length());
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        if (type == null || String.class.isAssignableFrom(type)) {
            return (T) getResource(url);

        } else if (Resource.class.isAssignableFrom(type)) {
            return (T) new ClasspathResource(url.substring(CLASSPATH.length()));

        } else {
            return (T) ConvertUtils.convert(url, type);

        }

    }

    static class ClasspathResource implements Resource {
        private final String path;

        ClasspathResource(String path) {
            this.path = path;
        }

        @Override
        public String getAsString(Charset encoding) throws ResourceException {
            try {
                return IOUtils.toString(getAsInputStream(), encoding);
            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }

        @Override
        public InputStream getAsInputStream() throws ResourceException {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        }
    }
}
