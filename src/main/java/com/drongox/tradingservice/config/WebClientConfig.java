package com.drongox.tradingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient quotesWebClient(WebClient.Builder builder)
    {
        return builder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Bean
    WebClient tradingCompanyWebClient(WebClient.Builder builder)
    {
        return builder
                .baseUrl("http://localhost:8082")
                .build();
    }
}
