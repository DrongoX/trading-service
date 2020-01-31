package com.drongox.tradingservice.tradingcompanysummary;

import com.drongox.tradingservice.quote.Quote;
import com.drongox.tradingservice.tradingcompany.TradingCompany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradingCompanySummary {
    private Quote quote;
    private TradingCompany tradingCompany;
}
