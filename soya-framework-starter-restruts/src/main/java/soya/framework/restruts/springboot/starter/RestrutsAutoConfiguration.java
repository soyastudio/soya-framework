package soya.framework.restruts.springboot.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.restruts.*;
import soya.framework.restruts.api.OpenApi300Publisher;
import soya.framework.restruts.api.SwaggerRenderer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Configuration
@EnableConfigurationProperties(RestrutsProperties.class)
public class RestrutsAutoConfiguration {
    private static Logger logger = Logger.getLogger(RestrutsAutoConfiguration.class.getName());

    @Autowired
    private RestrutsProperties properties;

    @Bean
    RestApiRenderer restApiRenderer() {
        return new SwaggerRenderer();
    }

    @Bean
    RestActionFactory actionFactory() {
        return new DefaultRestActionFactory();
    }

    @Bean
    DefaultRestActionContext restActionContext() {
        DefaultRestActionContext registration = new DefaultRestActionContext();
        registration.setApiPath(properties.getApiPath());
        return registration;
    }

    @Bean
    ServletRegistrationBean actionServlet(RestActionContext registration) {
        ServletRegistrationBean bean = new ServletRegistrationBean(new ActionServlet(registration),
                properties.getPath());
        bean.setLoadOnStartup(5);
        return bean;
    }

    @EventListener
    void onEvent(ApplicationStartedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Environment environment = context.getEnvironment();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(b -> {
            System.out.println("------------- " + b);
        });

        DefaultRestActionContext registration = context.getBean(DefaultRestActionContext.class);
        context.getBeansOfType(RestActionLoader.class).entrySet().forEach(e -> {
            registration.register(e.getValue());
        });

        context.getBeansOfType(RestActionFactory.class).entrySet().forEach(e -> {
            registration.register(e.getValue());
        });

        if (registration.getDependencyInjector() == null) {
            DefaultDependencyInjector injector = new DefaultDependencyInjector(context);
            context.getBeansOfType(DependencyInjector.class).entrySet().forEach(e -> {
                injector.add(e.getValue());
            });

            registration.register(injector);
        }

        if (properties.isAutoScan()) {
            DirectRestActionLoader loader = new DirectRestActionLoader();
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(RestAction.class, true, true));


            Map<String, RestActionDomain> domains = new HashMap<>();


            if (properties.getScanPackages() != null) {
                Arrays.stream(properties.getScanPackages().split(",")).forEach(pkg -> {
                    try {
                        Package pack = Class.forName(pkg + ".package-info").getPackage();
                        RestActionDomain domain = pack.getAnnotation(RestActionDomain.class);
                        System.out.println("============================ domain: " + domain);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    Set<BeanDefinition> set = scanner.findCandidateComponents(pkg.trim());
                    set.forEach(e -> {
                        try {


                            loader.add(Class.forName(e.getBeanClassName()));

                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                });
            }

            loader.load().forEach(e -> {
                registration.register(e);
            });
        }



        if (properties.getSpecification().equalsIgnoreCase("SWAGGER")) {
            registration.setApi(new SwaggerRenderer().render(registration));

        } else {
            registration.setApi(new OpenApi300Publisher().render(registration));
        }

    }
}
