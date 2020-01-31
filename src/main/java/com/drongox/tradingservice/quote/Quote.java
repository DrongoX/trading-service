package com.drongox.tradingservice.quote;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;

@NoArgsConstructor
@Data
public class Quote {

	private static final MathContext MATH_CONTEXT = new MathContext(2);

	private String ticker;
	private BigDecimal price;
	private Instant instant = Instant.now();

	public Quote(String ticker, BigDecimal price) {
		this.ticker = ticker;
		this.price = price;
	}

	public Quote(String ticker, Double price) {
		this(ticker, new BigDecimal(price, MATH_CONTEXT));
	}
}