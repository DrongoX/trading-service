package uk.zinch.workshop.tradingservice;

import uk.zinch.workshop.tradingservice.stockquotes.Quote;
import uk.zinch.workshop.tradingservice.stockquotes.StockQuotesRepository;
import uk.zinch.workshop.tradingservice.tradingcompany.TradingCompany;
import uk.zinch.workshop.tradingservice.tradingcompany.TradingCompanyNotFound;
import uk.zinch.workshop.tradingservice.tradingcompany.TradingCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TradingService {

  private final TradingCompanyRepository tradingCompanyRepository;
  private final StockQuotesRepository stockQuotesRepository;

  public Mono<TradingInfo> getActualTradingInfoFor(String ticker) {
    var latestQuote = getLatestQuote(ticker);
    var company = getTradingCompanyByTicker(ticker);
    return latestQuote.zipWith(company)
                      .map(tuple -> new TradingInfo(tuple.getT1(), tuple.getT2()));
  }

  private Mono<TradingCompany> getTradingCompanyByTicker(String ticker) {
    return tradingCompanyRepository.findTradingCompanyByTicker(ticker)
        .onErrorResume(TradingCompanyNotFound.class, err -> getFallbackTradingCompanyInCaseOfError(ticker));
  }

  private Mono<TradingCompany> getFallbackTradingCompanyInCaseOfError(String ticker) {
    return Mono.fromSupplier(() -> new TradingCompany("UNKNOWN", "No details available", ticker));
  }

  private Mono<Quote> getLatestQuote(String ticker) {
    return stockQuotesRepository.getQuotesFeed()
                                .filter(q -> ticker.equals(q.getTicker()))
                                .next()
                                .timeout(Duration.ofSeconds(3), Mono.fromSupplier(() -> new Quote(ticker, BigDecimal.ZERO, Instant.now())));
  }
}
