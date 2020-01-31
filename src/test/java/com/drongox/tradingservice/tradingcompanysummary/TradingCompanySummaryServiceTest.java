package com.drongox.tradingservice.tradingcompanysummary;

import com.drongox.tradingservice.quote.Quote;
import com.drongox.tradingservice.quote.QuoteService;
import com.drongox.tradingservice.tradingcompany.TradingCompany;
import com.drongox.tradingservice.tradingcompany.TradingCompanyClient;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TradingCompanySummaryServiceTest {

    @Test
    void should_retrieve_correct_summary_by_ticker() {
        QuoteService quoteService = mock(QuoteService.class);
        TradingCompanyClient tradingCompanyClient = mock(TradingCompanyClient.class);
        TradingCompanySummaryService service = new TradingCompanySummaryService(quoteService, tradingCompanyClient);
        String hppn = "HPPN";
        when(quoteService.getLatestQuote(eq(hppn)))
                .thenReturn(Mono.just(new Quote(hppn, 42.1)));
        when(tradingCompanyClient.getTradingCompanyByTicker(eq(hppn)))
                .thenReturn(Mono.just(new TradingCompany("1", "test", hppn)));

        Mono<TradingCompanySummary> result = service.getByTicker(hppn);

        StepVerifier.create(result)
                .consumeNextWith(s -> {
                    assertThat(s.getQuote().getTicker()).isEqualTo(hppn);
                    assertThat(s.getTradingCompany().getTicker()).isEqualTo(hppn);
                })
                .verifyComplete();
    }
}
