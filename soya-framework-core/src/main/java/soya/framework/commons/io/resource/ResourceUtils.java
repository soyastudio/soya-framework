package soya.framework.commons.io.resource;

import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.net.URI;

public class ResourceUtils {

    private static ResourceLoader loader = DefaultResourceLoader.getInstance();

    private ResourceUtils() {
    }

    public static Resource getResource(URI uri) throws ResourceException {
        return loader.load(uri);
    }


}
