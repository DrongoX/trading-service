package com.drongox.tradingservice.tradingcompany.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TradingCompanyRouter {
    @Bean
    RouterFunction<ServerResponse> tradingCompanyRoutes(TradingCompanyController tradingCompanyController) {
        return route(GET("/details"), tradingCompanyController::getAllTradingCompanies)
                .andRoute(GET("/details/{ticker}"), r -> tradingCompanyController.findByTicker(r.pathVariable("ticker")));
    }
}
