package soya.framework.commons.io.resource;

import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class DefaultResourceLoader implements ResourceLoader {

    private static ResourceLoader me;

    private Map<String, ResourceLoader> loaders = new HashMap<>();
    private URLResourceLoader defaultResourceLoader = new URLResourceLoader();

    static {
        me = new DefaultResourceLoader()
                .register(ClasspathResource.SCHEMA, ClasspathResource.loader());
    }

    public static ResourceLoader getInstance() {
        return me;
    }

    public DefaultResourceLoader register(String schema, ResourceLoader resourceLoader) {
        loaders.put(schema, resourceLoader);
        return this;
    }

    public DefaultResourceLoader register(String[] schema, ResourceLoader resourceLoader) {
        Arrays.stream(schema).forEach(e -> {
            loaders.put(e, resourceLoader);
        });
        return this;
    }

    @Override
    public Resource load(URI uri) throws ResourceException {
        String schema = uri.getScheme();
        if(loaders.containsKey(schema)) {
            return loaders.get(schema).load(uri);
        }

        return defaultResourceLoader.load(uri);
    }
}
