package soya.framework.action.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionRegistration;

@Configuration
@EnableConfigurationProperties(ActionProperties.class)
public class ActionAutoConfiguration {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ActionProperties properties;

    private SpringActionContext actionContext;

    @Bean
    ActionContext actionContext() {
        this.actionContext = new SpringActionContext();
        return actionContext;
    }

    @Bean
    ActionRegistration actionRegistration(ApplicationContext applicationContext) {
        ActionRegistration.Builder builder = ActionRegistration.builder();
        if (properties.getScanPackages() != null) {
            String[] pkgs = properties.getScanPackages().split(",");
            builder.register(new ActionDefinitionAnnotationScanner(pkgs));
        }

        return builder.create();
    }

    @EventListener
    public void onEvent(ApplicationStartedEvent event) {

    }
}
