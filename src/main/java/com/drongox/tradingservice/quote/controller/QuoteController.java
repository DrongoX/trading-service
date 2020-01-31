package com.drongox.tradingservice.quote.controller;

import com.drongox.tradingservice.quote.Quote;
import com.drongox.tradingservice.quote.QuotesClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Controller
public class QuoteController {

    private final QuotesClient quotesClient;

    public QuoteController(QuotesClient quotesClient) {
        this.quotesClient = quotesClient;
    }

    public Mono<ServerResponse> getQuotesStream(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(quotesClient.quotesFeed(), Quote.class);
    }
}
