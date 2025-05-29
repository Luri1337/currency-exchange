package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.exceptions.ExchangeRateNotFoundException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
// TODO: придумать логику для  "GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT"
// придумать как формировать ответ, понять нужна ли еще одна модель отдельно для exchange
public class ExchangeService {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public BigDecimal exchange(String from, String to, String amount) throws SQLException {

        if(!exchangeRateDao.getByCodePair(from, to).isEmpty()){
            return exchangeRateDao.getByCodePair(from, to).get().getRate().multiply(BigDecimal.valueOf(Integer.parseInt(amount)));
        }
        return BigDecimal.ZERO;

    }

}
