package soya.framework.restruts.springboot;

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
import org.springframework.core.type.filter.AnnotationTypeFilter;
import soya.framework.restruts.*;
import soya.framework.restruts.api.OpenApi300Publisher;
import soya.framework.restruts.api.OpenApiRenderer;
import soya.framework.restruts.api.SwaggerRenderer;
import soya.framework.restruts.resource.ClasspathResourceLoader;
import soya.framework.restruts.resource.EnvironmentResourceLoader;

import java.io.IOException;
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

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    RestApiRenderer restApiRenderer() {
        return new OpenApiRenderer();
    }

    @Bean
    DefaultRestActionContext restActionContext(ApplicationContext applicationContext) {
        DefaultRestActionContext registration = new DefaultRestActionContext(applicationContext);

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

        DefaultRestActionContext ctx = context.getBean(DefaultRestActionContext.class);
        context.getBeansOfType(RestActionLoader.class).entrySet().forEach(e -> {
            ctx.register(e.getValue());
        });

        // resource loaders:
        ctx.register(new ClasspathResourceLoader())
                        .register(new EnvironmentResourceLoader());
        context.getBeansOfType(ResourceLoader.class).entrySet().forEach(e -> {
            ctx.register(e.getValue());
        });

        if (properties.isAutoScan()) {
            DirectRestActionLoader loader = new DirectRestActionLoader();
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(RestAction.class, true, true));


            Map<String, RestActionDomain> domains = new HashMap<>();

            //
            if (properties.getScanPackages() != null) {
                Arrays.stream(properties.getScanPackages().split(",")).forEach(pkg -> {
                    try {
                        Package pack = Class.forName(pkg + ".package-info").getPackage();
                        RestActionDomain domain = pack.getAnnotation(RestActionDomain.class);
                        System.out.println("============================ domain: " + domain);
                    } catch (ClassNotFoundException e) {
                        // throw new RuntimeException(e);
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
                ctx.register(e);
            });
        }


        if (properties.getSpecification().equalsIgnoreCase("SWAGGER")) {
            ctx.setApi(new SwaggerRenderer().render(ctx));

        } else {
            try {
                ctx.setApi(new OpenApiRenderer().render(ctx));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
