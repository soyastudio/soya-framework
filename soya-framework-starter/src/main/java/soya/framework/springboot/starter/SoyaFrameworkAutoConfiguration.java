package soya.framework.springboot.starter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.commons.io.Resource;
import soya.framework.context.Container;
import soya.framework.context.ServiceLocateException;
import soya.framework.context.ServiceLocator;
import soya.framework.context.ServiceLocatorSingleton;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(SoyaFrameworkProperties.class)
public class SoyaFrameworkAutoConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    @Autowired
    private SoyaFrameworkProperties properties;

    @PostConstruct
    void initialize() {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(Container.class));

        String basePackage = "soya.framework";

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


        /*Set<String> packages = new LinkedHashSet<>();
        if (properties.getScanPackages() != null) {
            Arrays.stream(properties.getScanPackages().split(",")).forEach(e -> {
                packages.add(e.trim());
            });
        } else {
            packages.add("soya.framework");
        }

        new IndexedClassStore.DefaultIndexedClassStore(packages.toArray(new String[packages.size()]));
        new DefaultActionContext(this);*/
    }

    @EventListener
    public void onEvent(ApplicationStartedEvent event) {
        new SingletonServiceLocator(new ApplicationContextServiceLocator(event.getApplicationContext()));
    }

    static class SingletonServiceLocator extends ServiceLocatorSingleton {
        protected SingletonServiceLocator(ServiceLocator locator) {
            super(locator);
        }
    }

}
