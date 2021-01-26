package uk.zinch.workshop.tradingservice.tradingcompany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradingCompany {
  private String id, description, ticker;
}
