package uk.zinch.workshop.tradingservice.stockquotes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "uk.zinch.workshop:stock-quotes:+:8888")
class StockQuotesRepositoryTest {

  @Autowired
  private StockQuotesRepository repo;

  @Test
  @DisplayName("Should get quotes feed")
  void should_get_the_latest_quote() {
    //given
    //when
    Flux<Quote> quotes = repo.getQuotesFeed();
    //then
    StepVerifier.create(quotes)
                .assertNext(q -> assertThat(q.getPrice()).isPositive())
                .thenCancel()
                .verify();
  }
}
