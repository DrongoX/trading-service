package com.drongox.tradingservice.tradingcompany;


import com.drongox.tradingservice.tradingcompany.controller.TradingCompanyController;
import com.drongox.tradingservice.tradingcompany.controller.TradingCompanyRouter;
import org.junit.jupiter.api.Test;
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
import static reactor.core.publisher.Flux.just;

@WebFluxTest(TradingCompanyController.class)
@Import(TradingCompanyRouter.class)
class TradingCompanyApiTest {
    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private TradingCompanyClient tradingCompanyClient;

    @Test
    void should_return_all_trading_companies() {
        //given
        when(tradingCompanyClient.getAllTradingCompanies())
                .thenReturn(just(new TradingCompany("1", "Test", "TEST"),
                                 new TradingCompany("2", "Test2","TEST2")));
        //when
        Flux<TradingCompany> response = webTestClient.get()
                .uri("/details")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(TradingCompany.class)
                .getResponseBody();
        //then
        StepVerifier.create(response)
                .expectNextMatches(tc -> tc.getTicker().equals("TEST"))
                .expectNextMatches(tc -> tc.getTicker().equals("TEST2"))
                .verifyComplete();
    }

    @Test
    void should_return_trading_company_by_ticker() {
        //given
        when(tradingCompanyClient.getTradingCompanyByTicker(eq("TEST")))
                .thenReturn(Mono.just(new TradingCompany("1", "Test", "TEST")));
        //when
        Flux<TradingCompany> response = webTestClient.get()
                .uri("/details/{ticker}", "TEST")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(TradingCompany.class)
                .getResponseBody();
        //then
        StepVerifier.create(response)
                .expectNextMatches(tc -> tc.getTicker().equals("TEST"))
                .verifyComplete();
    }
}
