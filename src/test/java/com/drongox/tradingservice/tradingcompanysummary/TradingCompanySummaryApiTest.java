package com.drongox.tradingservice.tradingcompanysummary;

import com.drongox.tradingservice.quote.Quote;
import com.drongox.tradingservice.tradingcompany.TradingCompany;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(TradingCompanySummaryController.class)
@Import(TradingCompanySummaryRouter.class)
public class TradingCompanySummaryApiTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    TradingCompanySummaryService service;

    @Test
    void should_find_summary_by_ticker() {
        String hppn = "HPPN";
        when(service.getByTicker(eq(hppn)))
                        .thenReturn(Mono.just(new TradingCompanySummary(
                                new Quote(hppn, 42.0),
                                new TradingCompany("1", "test", hppn)
                        )));

        Flux<TradingCompanySummary> responseBody = webTestClient.get().uri(b -> b.path("/quotes/summary/{ticker}").build(hppn))
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(TradingCompanySummary.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .consumeNextWith(sum -> Assertions.assertThat(sum.getTradingCompany().getTicker()).isEqualTo(hppn))
                .verifyComplete();
    }
}
