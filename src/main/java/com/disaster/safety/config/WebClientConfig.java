package com.disaster.safety.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean("eyeWebClient")
    public WebClient eyeWebClient() {
        return WebClient.builder()
                .baseUrl("https://relic-unsecured-shrine.ngrok-free.dev/predict/eye")
                .build();
    }

    @Bean("skinWebClient")
    public WebClient skinWebClient() {
        return WebClient.builder()
                .baseUrl("https://relic-unsecured-shrine.ngrok-free.dev/predict/skin")
                .build();
    }
}
