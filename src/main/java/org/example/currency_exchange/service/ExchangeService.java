package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.Exchange;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;

// TODO: придумать логику для  "GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT"
// придумать как формировать ответ, понять нужна ли еще одна модель отдельно для exchange
public class ExchangeService {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    private BigDecimal getConvertedAmount(String from, String to, String amount) throws SQLException {

        if (!exchangeRateDao.getByCodePair(from, to).isEmpty()) {
            return exchangeRateDao.getByCodePair(from, to).get().getRate().multiply(BigDecimal.valueOf(Integer.parseInt(amount)));
        }
        if (!exchangeRateDao.getByCodePair(to, from).isEmpty()) {
            return (BigDecimal.ONE.divide(exchangeRateDao.getByCodePair(to, from).get().getRate())).multiply(BigDecimal.valueOf(Integer.parseInt(amount)));
        }
        if (!exchangeRateDao.getByCodePair("USD", from).isEmpty() && !exchangeRateDao.getByCodePair("USD", to).isEmpty()) {
            return (exchangeRateDao.getByCodePair("USD", to).get().getRate().divide(exchangeRateDao.getByCodePair("USD", from).get().getRate(), new MathContext(10, RoundingMode.HALF_UP)))
                    .multiply(BigDecimal.valueOf(Integer.parseInt(amount)));
        }
        return BigDecimal.ZERO;
    }

    public Exchange exchange(String from, String to, String amount) throws SQLException {
        return new Exchange((Optional<Currency>) currencyDao.getByCode(from), (Optional<Currency>) currencyDao.getByCode(to),
                getConvertedAmount(from, to, amount).divide(BigDecimal.valueOf(Integer.valueOf(amount))),
                BigDecimal.valueOf(Integer.valueOf(amount)), getConvertedAmount(from, to, amount));
    }


}
