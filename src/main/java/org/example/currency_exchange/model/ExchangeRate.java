package org.example.currency_exchange.model;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRate(int id, Optional<Currency> baseCurrencyId, Optional<Currency> targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrencyId.orElse(baseCurrency);
        this.targetCurrency = targetCurrencyId.orElse(targetCurrency);
        this.rate = rate;
    }
}



