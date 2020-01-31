package com.drongox.tradingservice.quote;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Service
public class QuotesClient {
    private final WebClient quotesClient;

    public QuotesClient(@Qualifier("quotesWebClient") WebClient quotesClient) {
        this.quotesClient = quotesClient;
    }

    public Flux<Quote> quotesFeed() {
        return quotesClient
                .get()
                .uri("/quotes")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Quote.class);
    }
}
