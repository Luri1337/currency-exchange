package org.example.currency_exchange.util.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateAlreadyExistException;
import org.example.currency_exchange.exception.exchangeRateException.InvalidExchangeRateFormatException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.AppMassages;

import java.sql.SQLException;

public class ExchangeRatesValidator extends Validator {
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private static final CurrencyDao currencyDao = new CurrencyDao();

    @Override
    public void validateRequest(HttpServletRequest request) throws SQLException {
        String baseCurrencyID = request.getParameter("baseCurrencyID");
        String targetCurrencyID = request.getParameter("targetCurrencyID");
        String rate = request.getParameter("rate");

        validateExchangeRate(baseCurrencyID, targetCurrencyID, rate);
    }

    private void validateExchangeRate(String baseCurrencyID, String targetCurrencyID, String rate) throws SQLException {
        if (baseCurrencyID.isBlank() || targetCurrencyID.isBlank() || rate.isBlank()) {
            throw new MissingRequiredParameterException(AppMassages.MISSING_REQUIRED_PARAMETER);
        }

        if (!baseCurrencyID.matches("^\\d+$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_ID_FORMAT);
        }
        if (!targetCurrencyID.matches("^\\d+$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_ID_FORMAT);
        }
        if (!rate.matches("^\\d+(\\.\\d+)?$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_RATE_FORMAT);
        }

        Currency baseCurrency = currencyDao.getById(Integer.parseInt(baseCurrencyID))
                .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

        Currency targetCurrency = currencyDao.getById(Integer.parseInt(targetCurrencyID))
                .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

        if (checkIfPresent(baseCurrency, targetCurrency)) {
            throw new ExchangeRateAlreadyExistException(AppMassages.EXCHANGE_RATE_ALREADY_EXISTS);
        }
    }

    private boolean checkIfPresent(Currency baseCurrency, Currency targetCurrency) throws SQLException {
        return exchangeRateDao.getByCodePair(baseCurrency.getCode(), targetCurrency.getCode()).isPresent();
    }
}