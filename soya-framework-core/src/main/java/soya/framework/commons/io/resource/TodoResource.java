package soya.framework.commons.io.resource;

import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.net.URI;
import java.nio.charset.Charset;

public class TodoResource extends AbstractResource<String> {
    public static final String SCHEMA = "todo";

    private static ResourceLoader loader = new TodoResourceLoader(new String[]{SCHEMA});

    protected TodoResource(URI uri) {
        super(uri);
    }

    @Override
    public String get() throws ResourceException {
        throw new ResourceException("TODO: resource is not specified");
    }

    @Override
    public String getAsString(Charset encoding) throws ResourceException {
        throw new ResourceException("TODO: resource is not specified");
    }

    public static ResourceLoader loader() {
        return loader;
    }

    static class TodoResourceLoader extends AbstractResourceLoader {

        protected TodoResourceLoader(String[] namespaces) {
            super(namespaces);
        }
    }


}
