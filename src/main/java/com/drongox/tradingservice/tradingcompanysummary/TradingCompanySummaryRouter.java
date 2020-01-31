package com.drongox.tradingservice.tradingcompanysummary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TradingCompanySummaryRouter {
    @Bean
    RouterFunction<ServerResponse> summaryRoutes(TradingCompanySummaryController controller)
    {
        return RouterFunctions.route()
                .GET("/quotes/summary/{ticker}", controller::getSummaryByTicker)
                .build();
    }
}
