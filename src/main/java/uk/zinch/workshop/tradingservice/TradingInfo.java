package uk.zinch.workshop.tradingservice;

import uk.zinch.workshop.tradingservice.stockquotes.Quote;
import uk.zinch.workshop.tradingservice.tradingcompany.TradingCompany;
import lombok.Value;

@Value
public class TradingInfo {
  Quote quote;
  TradingCompany tradingCompany;
}
