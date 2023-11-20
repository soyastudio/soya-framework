package soya.framework.commons.io.resource;

import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;
import soya.framework.context.ServiceLocatorSingleton;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class SqlResource extends AbstractResource<Object> {

    public static final String SCHEMA = "sql";

    public static final ResourceLoader loader = new SqlResourceLoader(new String[]{SCHEMA});

    private Resource wrappedResource;

    protected SqlResource(URI uri) {
        super(uri);
        try {
            this.wrappedResource = ServiceLocatorSingleton.getInstance().getResource(new URI(uri.getSchemeSpecificPart()));
        } catch (URISyntaxException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public Object get() throws ResourceException {
        // TODO:
        return null;
    }

    @Override
    public String getAsString(Charset encoding) throws ResourceException {
        // TODO:
        return null;
    }

    public static ResourceLoader loader() {
        return loader;
    }

    static class SqlResourceLoader extends AbstractResourceLoader {

        protected SqlResourceLoader(String[] namespaces) {
            super(namespaces);
        }
    }
}
