package org.example.currency_exchange.util.validation;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateAlreadyExistException;
import org.example.currency_exchange.exception.exchangeRateException.InvalidExchangeRateFormatException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.AppMassages;

import java.sql.SQLException;

public class ExchangeRatesValidator {
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private static final CurrencyDao currencyDao = new CurrencyDao();

    public void validateRequest(String baseCurrencyCode, String targetCurrencyCode, String rate) throws SQLException {

        if (baseCurrencyCode.isBlank() || targetCurrencyCode.isBlank() || rate.isBlank()) {
            throw new MissingRequiredParameterException(AppMassages.MISSING_REQUIRED_PARAMETER);
        }

        if (!baseCurrencyCode.matches("^[A-Z]{3}$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_ID_FORMAT);
        }
        if (!targetCurrencyCode.matches("^[A-Z]{3}$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_ID_FORMAT);
        }
        if (!rate.matches("^\\d+(\\.\\d+)?$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_RATE_FORMAT);
        }

        Currency baseCurrency = currencyDao.getByCode(baseCurrencyCode)
                .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

        Currency targetCurrency = currencyDao.getByCode(targetCurrencyCode)
                .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

        if (checkIfPresent(baseCurrency, targetCurrency)) {
            throw new ExchangeRateAlreadyExistException(AppMassages.EXCHANGE_RATE_ALREADY_EXISTS);
        }
    }

    private boolean checkIfPresent(Currency baseCurrency, Currency targetCurrency) throws SQLException {
        return exchangeRateDao.getByCodePair(baseCurrency.getCode(), targetCurrency.getCode()).isPresent();
    }
}