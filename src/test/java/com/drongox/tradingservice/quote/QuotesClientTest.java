package com.drongox.tradingservice.quote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWireMock
class QuotesClientTest {
    @Autowired
    private QuotesClient quotesClient;

    @Test
    void should_retrieve_quotes_from_quotes_microservice()
    {
        stubFor(get("/quotes").willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
                .withBody("[\n" +
                        "  {\n" +
                        "    \"ticker\": \"TEST\",\n" +
                        "    \"price\": \"42.42\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"ticker\": \"TEST2\",\n" +
                        "    \"price\": \"43.43\"\n" +
                        "  }\n" +
                        "]")));
        Flux<Quote> quotesFeed = quotesClient.quotesFeed();
        StepVerifier.create(quotesFeed)
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .consumeRecordedWith(list -> assertThat(list).allMatch(quote -> quote.getPrice().doubleValue() > 0))
                .thenCancel()
                .verify();
    }
}
