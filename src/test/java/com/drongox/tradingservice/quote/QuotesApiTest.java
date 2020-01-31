package com.drongox.tradingservice.quote;

import com.drongox.tradingservice.quote.controller.QuoteController;
import com.drongox.tradingservice.quote.route.QuotesRouter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static reactor.core.publisher.Flux.just;

@WebFluxTest(QuoteController.class)
@Import(QuotesRouter.class)
class QuotesApiTest {
    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private QuotesClient quotesClient;

    @Test
    void should_return_quotes_stream() {
        //given
        when(quotesClient.quotesFeed())
                .thenReturn(just(new Quote("TEST", 42.0), new Quote("TEST2", 41.0)));
        //when
        Flux<Quote> response = webTestClient.get()
                .uri("/quotes/feed")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON)
                .returnResult(Quote.class)
                .getResponseBody();
        //then
        StepVerifier.create(response)
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .expectRecordedMatches(list -> list.stream().allMatch(quote -> quote.getPrice().doubleValue() > 0))
                .consumeRecordedWith(list -> assertThat(list).allMatch(quote -> quote.getPrice().doubleValue() > 0))
                .thenCancel()
                .verify();
    }
}
