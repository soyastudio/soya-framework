package soya.framework.restruts.springboot;

import soya.framework.restruts.NamespaceAware;
import soya.framework.restruts.Resource;
import soya.framework.restruts.ResourceLoader;
import soya.framework.restruts.resource.URLResourceLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultResourceLoader implements ResourceLoader {

    private URLResourceLoader defaultLoader = new URLResourceLoader();
    private Map<String, ResourceLoader> loaders = new HashMap<>();

    DefaultResourceLoader() {
    }

    public Resource load(String location) {

        String ns = getNamespace(location);
        if (loaders.containsKey(ns)) {
            return loaders.get(ns).load(location);
        }

        return defaultLoader.load(location);
    }

    DefaultResourceLoader add(ResourceLoader resourceLoader) {
        if (resourceLoader instanceof NamespaceAware) {
            String[] ns = ((NamespaceAware) resourceLoader).getNamespaces();
            Arrays.stream(ns).forEach(e -> {
                loaders.put(e, resourceLoader);
            });
        }
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
