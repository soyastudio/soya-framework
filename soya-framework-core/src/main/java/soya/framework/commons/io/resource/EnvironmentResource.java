package soya.framework.commons.io.resource;

import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;
import soya.framework.context.ServiceLocatorSingleton;

import java.net.URI;
import java.nio.charset.Charset;

public class EnvironmentResource extends AbstractResource<String> {

    public static final String SCHEMA = "env";
    private static final ResourceLoader loader = new EnvironmentResourceLoader(new String[]{SCHEMA});

    private String propName;

    protected EnvironmentResource(URI uri) {
        super(uri);
        this.propName = uri.getSchemeSpecificPart();
    }

    @Override
    public String get() throws ResourceException {
        return ServiceLocatorSingleton.getInstance().getProperty(propName);
    }

    @Override
    public String getAsString(Charset encoding) throws ResourceException {
        return get();
    }

    public static ResourceLoader loader() {
        return loader;
    }

    static class EnvironmentResourceLoader extends AbstractResourceLoader {

        protected EnvironmentResourceLoader(String[] namespaces) {
            super(namespaces);
        }
    }
}
