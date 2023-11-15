package soya.framework.action.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import soya.framework.action.ActionContext;

@Configuration
@EnableConfigurationProperties(ActionProperties.class)
public class ActionAutoConfiguration {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ActionProperties properties;

    private SpringActionContext actionContext;

    private DefaultActionRegistration registration;

    @Bean
    ActionContext actionContext(ApplicationContext applicationContext) {
        this.actionContext = new SpringActionContext(applicationContext);
        return actionContext;
    }

    @Bean
    DefaultActionRegistration actionRegistration() {
        this.registration = new DefaultActionRegistration();
        return registration;
    }

    @EventListener
    public void onEvent(ApplicationStartedEvent event) {
        if (properties.getScanPackages() != null) {
            registration.register(properties.getScanPackages().split(","));
        }
    }
}
