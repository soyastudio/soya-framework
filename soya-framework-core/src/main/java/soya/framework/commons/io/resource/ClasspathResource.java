package soya.framework.commons.io.resource;

import org.apache.commons.io.IOUtils;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

public class ClasspathResource extends AbstractResource<InputStream> {

    public static final String SCHEMA = "classpath";
    private static ResourceLoader loader = new ClasspathResourceLoader(new String[]{SCHEMA});

    protected ClasspathResource(URI uri) {
        super(uri);
    }

    @Override
    public InputStream get() throws ResourceException {
        String location = getURI().getSchemeSpecificPart();
        if (location.contains("?")) {
            location = location.substring(0, location.indexOf('?'));
        }

        return Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
    }

    @Override
    public String getAsString(Charset encoding) throws ResourceException {
        try {
            return IOUtils.toString(get(), encoding);
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    public static ResourceLoader loader() {
        return loader;
    }

    static class ClasspathResourceLoader extends AbstractResourceLoader {
        ClasspathResourceLoader(String[] namespaces) {
            super(namespaces);
        }
    }
}
