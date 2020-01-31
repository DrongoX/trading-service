package com.drongox.tradingservice.tradingcompany;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWireMock(port = 8082)
class TradingCompanyClientTest {
    @Autowired
    private TradingCompanyClient tradingCompanyClient;

    @Test
    void should_find_all_trading_companies()
    {
        stubFor(get("/details").willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("[\n" +
                        "  {\n" +
                        "    \"id\": \"1\",\n" +
                        "    \"description\": \"Test desc1\",\n" +
                        "    \"ticker\": \"TEST\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\": \"2\",\n" +
                        "    \"description\": \"Test desc2\",\n" +
                        "    \"ticker\": \"TEST2\"\n" +
                        "  }\n" +
                        "]")));
        Flux<TradingCompany> quotesFeed = tradingCompanyClient.getAllTradingCompanies();
        StepVerifier.create(quotesFeed)
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .consumeRecordedWith(list -> assertThat(list).allMatch(tc -> tc.getTicker().contains("TEST")))
                .verifyComplete();
    }

    @Test
    void should_get_one_company_by_ticker()
    {
        stubFor(get("/details/TEST").willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\n" +
                        "    \"id\": \"1\",\n" +
                        "    \"description\": \"Test desc1\",\n" +
                        "    \"ticker\": \"TEST\"\n" +
                        "  }")));
        Mono<TradingCompany> company = tradingCompanyClient.getTradingCompanyByTicker("TEST");
        StepVerifier.create(company)
                .expectNextMatches(tc -> tc.getTicker().equals("TEST"))
                .verifyComplete();
    }
}
