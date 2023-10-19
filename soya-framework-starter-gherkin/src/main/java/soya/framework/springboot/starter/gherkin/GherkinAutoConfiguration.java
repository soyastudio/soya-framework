package soya.framework.springboot.starter.gherkin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soya.framework.gherkin.GherkinEngine;

import java.util.logging.Logger;

@Configuration
@EnableConfigurationProperties(GherkinProperties.class)
public class GherkinAutoConfiguration {
    private static Logger logger = Logger.getLogger(GherkinAutoConfiguration.class.getName());

    @Autowired
    private GherkinProperties properties;

    @Bean
    GherkinEngine gherkinEngine() {
        logger.info("Initializing GherkinEngine...");
        return GherkinEngine
                .initializer()
                .create();
    }

    @Bean
    ServletRegistrationBean actionServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new GherkinServlet(),
                properties.getPath());
        bean.setLoadOnStartup(5);
        return bean;
    }


}
