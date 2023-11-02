package soya.framework.restruts.io;

import soya.framework.restruts.DependencyInjector;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlResourceInjector implements DependencyInjector {
    private static final String HTTP = "http:";
    private static final String HTTPS = "https";
    private static final String[] NAMESPACES = {
            HTTP,
            HTTPS
    };

    @Override
    public String[] getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public URL getWiredResource(String name) {
        try {
            return new URL(name);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getWiredResource(String resource, Class<T> type) {
        if(URL.class.isAssignableFrom(type)) {
            return (T) getWiredResource(resource);

        } else

        return null;
    }
}
