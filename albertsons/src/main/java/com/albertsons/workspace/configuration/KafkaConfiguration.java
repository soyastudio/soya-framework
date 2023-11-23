package com.albertsons.workspace.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import soya.framework.kafka.KafkaClient;
import soya.framework.kafka.KafkaClientFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Configuration
public class KafkaConfiguration {

    @Autowired
    Environment environment;

    @Bean
    KafkaClientFactory kafkaClientFactory() throws IOException {
        DefaultKafkaClientFactory factory = new DefaultKafkaClientFactory();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String[] conf = environment.getProperty("soya.framework.action.kafka.factory").split(",");
        Arrays.stream(conf).forEach(e -> {
            InputStream inputStream = classLoader.getResourceAsStream(e.trim());
            Properties properties = new Properties();
            try {
                properties.load(inputStream);
                factory.add(properties);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        return factory;

    }

    static class DefaultKafkaClientFactory implements KafkaClientFactory {

        private Map<String, KafkaClient> clients = new HashMap<>();

        DefaultKafkaClientFactory() {

        }

        DefaultKafkaClientFactory add(Properties properties) {
            String name = properties.getProperty("name");
            clients.put(name, new DefaultKafkaClient(properties));

            return this;
        }


        @Override
        public KafkaClient getKafkaClient(String name) {
            Objects.requireNonNull(name);

            String key = name.toUpperCase();
            if (!clients.containsKey(key)) {
                throw new IllegalArgumentException("Kafka client is not defined: " + name);
            }
            return clients.get(key);
        }
    }
}
