package uk.zinch.workshop.tradingservice.tradingcompany;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class TradingCompanyRepository {

  private final WebClient tradingCompanyWebClient;

  public Mono<TradingCompany> findTradingCompanyByTicker(String ticker) {
    return tradingCompanyWebClient.get()
                                  .uri(builder -> builder.path("/trading-companies/{ticker}").build(ticker))
                                  .accept(MediaType.APPLICATION_JSON)
                                  .retrieve()
                                  .onStatus(HttpStatus::is4xxClientError, resp -> Mono.error(TradingCompanyNotFound::new))
                                  .onStatus(HttpStatus::is5xxServerError, resp -> Mono.error(TradingCompanyServerError::new))
                                  .bodyToMono(TradingCompany.class)
                                  .retryWhen(Retry.backoff(5, Duration.ofMillis(100))
                                                  .filter(ex -> ex instanceof TradingCompanyServerError)
                                                  .onRetryExhaustedThrow((x, y) -> new TradingCompanyNotFound()));
  }
}
