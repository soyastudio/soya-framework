package com.albertsons.workspace.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soya.framework.action.ActionRegistration;
import soya.framework.action.rest.ActionRestAdapter;

@Configuration
public class RestConfiguration {

    @Bean
    ActionRestAdapter actionRestAdapter(@Autowired ActionRegistration actionRegistration) {
        return new ActionRestAdapter(actionRegistration);
    }

}
