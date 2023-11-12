package soya.framework.commons.io.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.commons.io.NamespaceAware;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ClasspathResourceLoader implements NamespaceAware, ResourceLoader {

    private static final String CLASSPATH = "classpath:";
    public static final String[] NAMESPACES = {CLASSPATH};

    @Override
    public String[] getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public Resource load(String location) throws ResourceException {
        return new ClasspathResource(location);
    }

    static class ClasspathResource implements Resource {
        private final String path;

        ClasspathResource(String location) {
            if (!location.startsWith(CLASSPATH)) {
                throw new IllegalArgumentException("Illegal path format: " + location);
            }
            this.path = location.substring(CLASSPATH.length());
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
