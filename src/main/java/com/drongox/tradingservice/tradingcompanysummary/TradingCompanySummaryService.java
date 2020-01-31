package com.drongox.tradingservice.tradingcompanysummary;

import com.drongox.tradingservice.quote.Quote;
import com.drongox.tradingservice.quote.QuoteService;
import com.drongox.tradingservice.tradingcompany.TradingCompany;
import com.drongox.tradingservice.tradingcompany.TradingCompanyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradingCompanySummaryService {

    private final QuoteService quoteService;
    private final TradingCompanyClient tradingCompanyClient;

    public Mono<TradingCompanySummary> getByTicker(String ticker) {
        Mono<Quote> latestQuote = quoteService.getLatestQuote(ticker);
        Mono<TradingCompany> tradingCompany = tradingCompanyClient.getTradingCompanyByTicker(ticker);
        return latestQuote.zipWith(tradingCompany).map(tuple -> new TradingCompanySummary(tuple.getT1(), tuple.getT2()));
    }
}
