package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeDto;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateNotFoundException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.util.AppMassages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeService {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    private BigDecimal calculateExchangeRate(String from, String to) throws SQLException {
        //прямой курс
        Optional<ExchangeRate> directRate = exchangeRateDao.getByCodePair(from, to);
        if (directRate.isPresent()) {
            return directRate.get().getRate();
        }

        //Обратный курс
        Optional<ExchangeRate> reverseRate = exchangeRateDao.getByCodePair(to, from);
        if (reverseRate.isPresent()) {
            return (BigDecimal.ONE
                    .divide(reverseRate.get().getRate(), 2, RoundingMode.HALF_UP));
        }

        //кросс курс через USD - A, USD - B
        Optional<ExchangeRate> usdFromRate = exchangeRateDao.getByCodePair("USD", from);
        Optional<ExchangeRate> usdToRate = exchangeRateDao.getByCodePair("USD", to);
        if (usdFromRate.isPresent() && usdToRate.isPresent()) {
            return usdToRate.get().getRate().divide(usdFromRate.get().getRate(), 2, RoundingMode.HALF_UP);
        }
        throw new ExchangeRateNotFoundException(AppMassages.EXCHANGE_RATE_NOT_FOUND);
    }

    public ExchangeDto exchange(String from, String to, String amount) throws SQLException {
        Currency fromCurrency = currencyDao.getByCode(from).orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));
        Currency toCurrency = currencyDao.getByCode(to).orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

        BigDecimal amountValue = new BigDecimal(amount);
        BigDecimal rate = calculateExchangeRate(from, to);
        BigDecimal convertedAmount = rate.multiply(amountValue).setScale(2, RoundingMode.HALF_UP);
        return new ExchangeDto(fromCurrency,
                toCurrency,
                rate,
                amountValue,
                convertedAmount);
    }

}
