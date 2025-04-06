package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConfig {
    @Value("${ds.api.url}")
    private String dsApiUrl;

    @Value("${ds.api.key}")
    private String dsApiKey;

    @Bean
    public WebClient dsWebClient() {
        return WebClient.builder()
                .baseUrl(dsApiUrl)
                .defaultHeader("Authorization", "Bearer " + dsApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
