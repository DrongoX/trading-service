package uk.zinch.workshop.tradingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@Configuration
public class WebClientConfiguration {
  @Bean
  WebClient tradingCompanyWebClient(WebClient.Builder builder) {
    return builder.baseUrl("http://localhost:8080")
                  .build();
  }

  @Bean
  WebClient stockQuotesWebClient(WebClient.Builder builder) {
    return builder.baseUrl("http://localhost:8888")
                  .defaultHeader(ACCEPT, APPLICATION_NDJSON_VALUE)
                  .build();
  }
}
