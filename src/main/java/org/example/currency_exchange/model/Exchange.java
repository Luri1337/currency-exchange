package org.example.currency_exchange.model;

import jakarta.servlet.annotation.WebServlet;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Exchange {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public Exchange(Optional<Currency> baseCurr, Optional<Currency> targetCurr, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurr.orElse(baseCurrency);
        this.targetCurrency = targetCurr.orElse(targetCurrency);
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
