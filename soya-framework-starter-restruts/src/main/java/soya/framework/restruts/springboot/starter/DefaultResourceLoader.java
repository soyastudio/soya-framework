package soya.framework.restruts.springboot.starter;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import soya.framework.restruts.ResourceLoader;
import soya.framework.restruts.io.ClasspathResourceLoader;
import soya.framework.restruts.io.FileResourceLoader;
import soya.framework.restruts.io.UrlResourceLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultResourceLoader implements ResourceLoader {

    private Map<String, ResourceLoader> injectors = new HashMap<>();

    private ApplicationContext applicationContext;

    DefaultResourceLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.add(new ClasspathResourceLoader())
                .add(new FileResourceLoader())
                .add(new UrlResourceLoader());
    }

    @Override
    public String[] getNamespaces() {
        return null;
    }

    @Override
    public Object getWiredResource(String name) {
        try {
            return applicationContext.getBean(name);

        } catch (Exception e) {
            try {
                return applicationContext.getBean(Class.forName(name));

            } catch (ClassNotFoundException ex) {
                throw new ResourceNotFoundException("Resource is not found: " + name);
            }
        }
    }

    DefaultResourceLoader add(ResourceLoader injector) {
        Arrays.stream(injector.getNamespaces()).forEach(e -> {
            injectors.put(e, injector);
        });
        return this;
    }

    @Override
    public <T> T getWiredResource(String resource, Class<T> type) {
        if (resource == null || resource.trim().length() == 0) {
            return applicationContext.getBean(type);

        } else if (resource.contains(":")) {
            return injectors.get(getNamespace(resource)).getWiredResource(resource, type);

        } else {
            if (String.class.isAssignableFrom(type)) {
                String prop = applicationContext.getEnvironment().getProperty(resource);
                if (prop == null) {
                    prop = System.getProperty(resource);
                }

                return (T) prop;

            } else {
                try {
                    return applicationContext.getBean(resource, type);

                } catch (NoSuchBeanDefinitionException ex) {
                    return applicationContext.getBean(type);

                }
            }
        }
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
