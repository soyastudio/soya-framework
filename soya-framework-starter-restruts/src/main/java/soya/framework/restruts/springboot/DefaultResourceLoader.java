package soya.framework.restruts.springboot;

import soya.framework.restruts.NamespaceResourceLoader;
import soya.framework.restruts.ResourceLoader;
import soya.framework.restruts.resource.UrlResourceLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultResourceLoader implements ResourceLoader {

    private UrlResourceLoader defaultLoader = new UrlResourceLoader();
    private Map<String, ResourceLoader> loaders = new HashMap<>();

    DefaultResourceLoader() {
    }

    @Override
    public Object getResource(String url) {
        String ns = getNamespace(url);
        if(loaders.containsKey(ns)) {
            return loaders.get(ns).getResource(url);
        }

        return defaultLoader.getResource(url);
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        String ns = getNamespace(url);
        if(loaders.containsKey(ns)) {
            return loaders.get(ns).getResource(url, type);
        }

        return defaultLoader.getResource(url, type);
    }

    DefaultResourceLoader add(NamespaceResourceLoader resourceLoader) {
        Arrays.stream(resourceLoader.getNamespaces()).forEach(e -> {
            loaders.put(e, resourceLoader);
        });
        return this;
    }

    private String getNamespace(String url) {
        int index = url.indexOf(":");
        if (index > 0) {
            return url.substring(0, index + 1);
        } else {
            return null;
        }
    }
}
