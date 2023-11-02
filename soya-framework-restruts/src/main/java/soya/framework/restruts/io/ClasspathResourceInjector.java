package soya.framework.restruts.io;

import org.apache.commons.io.IOUtils;
import soya.framework.restruts.DependencyInjector;

import java.io.IOException;
import java.io.InputStream;

public class ClasspathResourceInjector implements DependencyInjector {

    private static final String CLASSPATH = "classpath:";
    private static final String[] NAMESPACES = {CLASSPATH};

    @Override
    public String[] getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public String getWiredResource(String resource) {
        try {
            String path = resource.substring(CLASSPATH.length());
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getWiredResource(String resource, Class<T> type) {
        System.out.println("======================== ClasspathResourceInjector");
        if (String.class.isAssignableFrom(type)) {
            return (T) getWiredResource(resource);
        }

        return null;
    }
}
