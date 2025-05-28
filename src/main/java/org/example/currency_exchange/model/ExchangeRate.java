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

    public ExchangeRate(int id, Optional<Currency> baseCurr, Optional<Currency> targetCurr, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurr.orElse(baseCurrency);
        this.targetCurrency = targetCurr.orElse(targetCurrency);
        this.rate = rate;
    }

    public ExchangeRate(Optional<Currency> baseCurr, Optional<Currency> targetCurr, BigDecimal rate) {
        this.baseCurrency = baseCurr.orElse(baseCurrency);
        this.targetCurrency = targetCurr.orElse(targetCurrency);
        this.rate = rate;
    }
}



