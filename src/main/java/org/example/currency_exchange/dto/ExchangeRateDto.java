package org.example.currency_exchange.dto;

import lombok.*;
import org.example.currency_exchange.model.Currency;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRateDto {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
}
