package soya.framework.restruts.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.NamespaceAware;
import soya.framework.restruts.Resource;
import soya.framework.restruts.ResourceException;
import soya.framework.restruts.ResourceLoader;


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
            if(!location.startsWith(CLASSPATH)) {
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
