package com.drongox.tradingservice.tradingcompany;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TradingCompanyClient {
    private final WebClient tradingCompanyWebClient;

    public TradingCompanyClient(@Qualifier("tradingCompanyWebClient") WebClient tradingCompanyWebClient) {
        this.tradingCompanyWebClient = tradingCompanyWebClient;
    }

    public Flux<TradingCompany> getAllTradingCompanies() {
        return tradingCompanyWebClient.get()
                .uri("/details")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(TradingCompany.class);
    }

    public Mono<TradingCompany> getTradingCompanyByTicker(String ticker) {
        return tradingCompanyWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/details/{ticker}").build(ticker))
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(TradingCompany.class);
    }
}
