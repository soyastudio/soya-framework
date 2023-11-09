package soya.framework.action.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionDefinition;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;

@Configuration
@EnableConfigurationProperties(ActionProperties.class)
public class ActionAutoConfiguration {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ActionProperties properties;

    private DefaultActionContext actionContext;

    @Bean
    ActionContext actionContext(ApplicationContext applicationContext) {
        this.actionContext = new DefaultActionContext(applicationContext);
        return actionContext;
    }

    @EventListener
    public void  onEvent(ApplicationStartedEvent event) {

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ActionDefinition.class, true, true));

        if(properties.getScanPackages() != null) {
            Arrays.stream(properties.getScanPackages().split(",")).forEach(pkg -> {
                Set<BeanDefinition> set = scanner.findCandidateComponents(pkg.trim());
                set.forEach(e -> {
                    try {
                        ActionClass.register((Class<? extends Callable>) Class.forName(e.getBeanClassName()));
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            });
        }
    }
}
