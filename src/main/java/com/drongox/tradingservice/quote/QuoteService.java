package com.drongox.tradingservice.quote;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuotesClient quotesClient;

    public Mono<Quote> getLatestQuote(String ticker) {
        return quotesClient.quotesFeed()
                .filter(q -> Objects.equals(q.getTicker(), ticker))
                .next()
                .timeout(Duration.ofSeconds(5))
                .onErrorReturn(new Quote(ticker, 0.00));
    }
}
