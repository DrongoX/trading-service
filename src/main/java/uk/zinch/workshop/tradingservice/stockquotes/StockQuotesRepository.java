package uk.zinch.workshop.tradingservice.stockquotes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class StockQuotesRepository {

  private final WebClient stockQuotesWebClient;

  public Flux<Quote> getQuotesFeed() {
    return stockQuotesWebClient.get()
                               .uri("/quotes")
                               .retrieve()
                               .bodyToFlux(Quote.class);
  }
}
