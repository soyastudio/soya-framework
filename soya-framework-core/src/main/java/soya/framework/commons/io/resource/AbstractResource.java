package soya.framework.commons.io.resource;

import soya.framework.commons.io.NamespaceAware;
import soya.framework.commons.io.Resource;
import soya.framework.commons.io.ResourceException;
import soya.framework.commons.io.ResourceLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

public abstract class AbstractResource<T> implements Resource<T> {
    private final URI uri;

    protected AbstractResource(URI uri) {
        this.uri = uri;
    }

    public URI getURI() {
        return uri;
    }

    protected static abstract class AbstractResourceLoader implements NamespaceAware, ResourceLoader {
        private final String[] namespaces;
        private Constructor constructor;
        protected AbstractResourceLoader(String[] namespaces) {
            this.namespaces = namespaces;
            try {
                this.constructor = getClass().getDeclaringClass().getDeclaredConstructor(URI.class);
            } catch (NoSuchMethodException e) {
                throw new ResourceException(e);
            }
        }

        @Override
        public String[] getNamespaces() {
            return namespaces;
        }

        @Override
        public Resource load(URI uri) throws ResourceException {
            try {
                return  (Resource) constructor.newInstance(uri);

            } catch (InstantiationException | IllegalAccessException |InvocationTargetException e) {
                throw new ResourceException(e);
            }
        }
    }
}
