package uk.zinch.workshop.tradingservice;

import uk.zinch.workshop.tradingservice.stockquotes.Quote;
import uk.zinch.workshop.tradingservice.stockquotes.StockQuotesRepository;
import uk.zinch.workshop.tradingservice.tradingcompany.TradingCompany;
import uk.zinch.workshop.tradingservice.tradingcompany.TradingCompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TradingServiceTest {

  private static final String TICKER = "TICK";

  @Mock
  TradingCompanyRepository tradingCompanyRepository;
  @Mock
  StockQuotesRepository stockQuotesRepository;

  @InjectMocks
  TradingService service;

  @Test
  @DisplayName("Should fetch actual trading info")
  void should_emit_a_stream_of_trading_quotes() {
    //given
    mockTradingCompanyRepo();
    mockQuotesRepo();
    //when
    Mono<TradingInfo> tradingInfo = service.getActualTradingInfoFor(TICKER);
    //then
    StepVerifier.create(tradingInfo)
                .assertNext(ti -> {
                  assertThat(ti.getQuote()).isNotNull();
                  assertThat(ti.getQuote().getTicker()).isEqualTo(TICKER);
                  assertThat(ti.getTradingCompany()).isNotNull();
                  assertThat(ti.getTradingCompany().getTicker()).isEqualTo(TICKER);
                })
                .verifyComplete();
  }


  @Test
  @DisplayName("Should return default Quote if it took too long to obtain one")
  void should_return_default_quote_if_it_took_too_long_to_obtain_one() {
    //given
    mockTradingCompanyRepo();
    mockQuotesRepoNeverEmit();
    //when
    StepVerifier.withVirtualTime(() -> service.getActualTradingInfoFor(TICKER))
                //then
                .thenAwait(Duration.ofSeconds(10))
                .assertNext(ti -> {
                  assertThat(ti.getQuote()).isNotNull();
                  assertThat(ti.getQuote().getPrice()).isEqualTo(BigDecimal.ZERO);
                  assertThat(ti.getQuote().getTicker()).isEqualTo(TICKER);
                  assertThat(ti.getTradingCompany()).isNotNull();
                  assertThat(ti.getTradingCompany().getTicker()).isEqualTo(TICKER);
                })
                .verifyComplete();
  }

  private void mockQuotesRepoNeverEmit() {
    when(stockQuotesRepository.getQuotesFeed()).thenReturn(Flux.never());
  }

  private void mockQuotesRepo() {
    when(stockQuotesRepository.getQuotesFeed())
        .thenReturn(Flux.just(new Quote("COUCOU", BigDecimal.TEN, Instant.now()),
                              new Quote(TICKER, BigDecimal.ONE, Instant.now())));
  }

  private void mockTradingCompanyRepo() {
    when(tradingCompanyRepository.findTradingCompanyByTicker(any()))
        .thenAnswer(ic -> Mono.just(new TradingCompany("ID", "blabla", ic.getArgument(0))));
  }
}
