package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeDto;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.AppMassages;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;

public class ExchangeService {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    private BigDecimal getConvertedAmount(String from, String to, String amount) throws SQLException {
        //первый вариант перевода, когда в бд есть этот курс
        if (exchangeRateDao.getByCodePair(from, to).isPresent()) {
            return exchangeRateDao.getByCodePair(from, to).get().getRate()
                    .multiply(BigDecimal.valueOf(Integer.parseInt(amount)));
        }
        //второй вариант, когда в бд есть обратный курс
        if (exchangeRateDao.getByCodePair(to, from).isPresent()) {
            return (BigDecimal.ONE
                    .divide(exchangeRateDao.getByCodePair(to, from).get().getRate(), new MathContext(2, RoundingMode.HALF_UP)))
                    .multiply(BigDecimal.valueOf(Integer.parseInt(amount)), new MathContext(2, RoundingMode.HALF_UP));
        }
        //третий варик, когда в бд есть курс USD - A, USD - B
        if (exchangeRateDao.getByCodePair("USD", from).isPresent()
                && exchangeRateDao.getByCodePair("USD", to).isPresent()) {
            return (exchangeRateDao.getByCodePair("USD", to).get().getRate()
                    .divide(exchangeRateDao.getByCodePair("USD", from).get().getRate(), new MathContext(2, RoundingMode.HALF_UP)))
                    .multiply(BigDecimal.valueOf(Integer.parseInt(amount)), new MathContext(2, RoundingMode.HALF_UP));
        }
        return BigDecimal.ZERO;
    }

    public ExchangeDto exchange(String from, String to, String amount) throws SQLException {
        Currency fromCurrency = currencyDao.getByCode(from).orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));
        Currency toCurrency = currencyDao.getByCode(to).orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

        return new ExchangeDto(fromCurrency,
                toCurrency,
                getConvertedAmount(from, to, amount)
                        .divide(BigDecimal.valueOf(Integer.parseInt(amount))),
                BigDecimal.valueOf(Integer.parseInt(amount)),
                getConvertedAmount(from, to, amount));
    }

}
