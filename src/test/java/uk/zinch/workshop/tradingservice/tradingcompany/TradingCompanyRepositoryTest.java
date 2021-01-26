package uk.zinch.workshop.tradingservice.tradingcompany;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "uk.zinch.workshop:trading-company:+:8080")
class TradingCompanyRepositoryTest {

  @Autowired
  private TradingCompanyRepository repo;

  @Test
  @DisplayName("Should find a Trading Company by ticker")
  void should_find_a_trading_company_by_ticker() {
    //given
    //when
    Mono<TradingCompany> found = repo.findTradingCompanyByTicker("TOCK");
    //then
    StepVerifier.create(found)
                .assertNext(tc -> {
                  assertThat(tc.getTicker()).isNotBlank();
                  assertThat(tc.getId()).isNotBlank();
                })
                .verifyComplete();
  }

  @Test
  @DisplayName("Should emit error if Trading Company not found")
  void should_emit_error_if_trading_company_not_found() {
    //given
    var ticker = "NOTFOUND";
    //when
    Mono<TradingCompany> notFound = repo.findTradingCompanyByTicker(ticker);
    //then
    StepVerifier.create(notFound)
                .verifyError(TradingCompanyNotFound.class);
  }


  @Test
  @DisplayName("Should retry if server error")
  void should_retry_if_server_is_down() {
    //given
    var ticker = "ERROR";
    //when
    Mono<TradingCompany> notFound = repo.findTradingCompanyByTicker(ticker);
    //then
    StepVerifier.create(notFound)
                .verifyError(TradingCompanyNotFound.class);
  }
}
