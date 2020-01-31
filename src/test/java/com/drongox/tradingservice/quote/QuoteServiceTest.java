package com.drongox.tradingservice.quote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock
    private QuotesClient quotesClient;

    @Test
    void should_retrieve_first_quote_summary()
    {
        //given
        QuoteService quoteService = new QuoteService(quotesClient);
        when(quotesClient.quotesFeed())
                .thenReturn(Flux.just(new Quote("GOOG", 2.2),
                                      new Quote("HPPN", 3.3),
                                      new Quote("HPPN", 4.4)));

        Mono<Quote> latestQuote = quoteService.getLatestQuote("HPPN");

        StepVerifier.create(latestQuote)
                .consumeNextWith(q -> {
                    assertThat(q.getTicker()).isEqualTo("HPPN");
                    assertThat(q.getPrice()).isEqualTo(valueOf(3.3));
                })
                .verifyComplete();
    }

    @Test
    void should_retrieve_default_summary_if_timeout()
    {
        //given
        QuoteService quoteService = new QuoteService(quotesClient);
        when(quotesClient.quotesFeed())
                .thenReturn(Flux.just(new Quote("GOOG", 2.2)).delayElements(Duration.ofSeconds(10)));

        Mono<Quote> latestQuote = quoteService.getLatestQuote("HPPN");

        StepVerifier.create(latestQuote)
                .consumeNextWith(q -> {
                    assertThat(q.getTicker()).isEqualTo("HPPN");
                    assertThat(q.getPrice()).isEqualTo(ZERO);
                })
                .verifyComplete();
    }
}
