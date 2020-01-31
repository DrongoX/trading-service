package com.drongox.tradingservice.tradingcompanysummary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Controller
@RequiredArgsConstructor
public class TradingCompanySummaryController {

    private final TradingCompanySummaryService service;

    public Mono<ServerResponse> getSummaryByTicker(ServerRequest serverRequest) {
        return service.getByTicker(serverRequest.pathVariable("ticker"))
                .flatMap(found -> ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(found), TradingCompanySummary.class))
                .onErrorMap(ex -> ex instanceof WebClientResponseException &&
                        ((WebClientResponseException) ex).getStatusCode().is4xxClientError(),
                        OurCustomException::new)
                .onErrorResume(OurCustomException.class, ex -> notFound().build());
    }

    private static class OurCustomException extends RuntimeException {
        public OurCustomException(Throwable cause) {
            super(cause);
        }
    }
}
