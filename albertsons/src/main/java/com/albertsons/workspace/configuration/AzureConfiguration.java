package com.albertsons.workspace.configuration;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AzureConfiguration {

    @Autowired
    Environment environment;

    @Bean
    BlobServiceClient blobServiceClient() {

        StringBuilder builder = new StringBuilder()
                .append("DefaultEndpointsProtocol=https;")
                .append("AccountName=").append(environment.getProperty("spring.cloud.azure.storage.blob.account-name")).append(";")
                .append("AccountKey=").append(environment.getProperty("spring.cloud.azure.storage.blob.account-key")).append(";");

        return new BlobServiceClientBuilder()
                .connectionString(builder.toString())
                .endpoint(environment.getProperty("spring.cloud.azure.storage.blob.endpoint")).buildClient();
    }
}
