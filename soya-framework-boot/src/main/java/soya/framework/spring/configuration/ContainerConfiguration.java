package soya.framework.spring.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.container.Container;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Set;

@Configuration
public class ContainerConfiguration {

    @PostConstruct
    void init() {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(Container.class));

        String basePackage = "soya";

        PathMatchingResourcePatternResolver resourceResolver = (PathMatchingResourcePatternResolver) provider.getResourceLoader();
/*

        URLClassLoader urlClassLoader = (URLClassLoader) resourceResolver.getClassLoader();
        URL[] urls = urlClassLoader.getURLs();
        Arrays.stream(urls).forEach(e -> {
            System.out.println("=========== url: " + e);

        });
*/


        Set<BeanDefinition> components = provider.findCandidateComponents(basePackage);
        for (BeanDefinition component : components) {
            System.out.println("=============== " + component.getBeanClassName());
            //System.out.printf("Component: %s\n", component.getBeanClassName());
        }
    }
}
