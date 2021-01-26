package uk.zinch.workshop.tradingservice.stockquotes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quote {
  private String ticker;
  private BigDecimal price;
  private Instant instant;
}
