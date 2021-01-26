package uk.zinch.workshop.tradingservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class Routes {
  @Bean
  RouterFunction<ServerResponse> tradingRoutes(TradingService service) {
    return route()
        .GET("/trading/{ticker}",
             r -> ok().body(service.getActualTradingInfoFor(r.pathVariable("ticker")), TradingInfo.class))
        .build();
  }
}
