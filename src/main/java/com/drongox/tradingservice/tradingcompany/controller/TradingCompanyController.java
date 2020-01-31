package com.drongox.tradingservice.tradingcompany.controller;

import com.drongox.tradingservice.tradingcompany.TradingCompany;
import com.drongox.tradingservice.tradingcompany.TradingCompanyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class TradingCompanyController {

    private final TradingCompanyClient tradingCompanyClient;

    public Mono<ServerResponse> getAllTradingCompanies(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tradingCompanyClient.getAllTradingCompanies(), TradingCompany.class);
    }

    public Mono<ServerResponse> findByTicker(String ticker) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tradingCompanyClient.getTradingCompanyByTicker(ticker), TradingCompany.class);
    }
}
