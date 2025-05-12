package org.example.currency_exchange.model;

import java.math.BigDecimal;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private int id;
    private int baseCurrencyID;
    private int targetCurrencyID;
    private BigDecimal rate;

    public ExchangeRate(int baseCurrencyID, int targetCurrencyID, BigDecimal rate) {
        this.baseCurrencyID = baseCurrencyID;
        this.targetCurrencyID = targetCurrencyID;
        this.rate = rate;
    }
}



