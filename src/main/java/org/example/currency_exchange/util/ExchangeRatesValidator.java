package org.example.currency_exchange.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exception.*;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateAlreadyExistException;
import org.example.currency_exchange.exception.exchangeRateException.InvalidExchangeRateFormatException;
import org.example.currency_exchange.model.Currency;

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
        if (baseCurrencyID.isBlank() || targetCurrencyID.isBlank() || rate == null || rate.isBlank()) {
            throw new MissingRequiredParameterException("Missing required exchangeRate field");
        }

        if(!baseCurrencyID.matches("^\\d+$")){
            throw new InvalidExchangeRateFormatException("Invalid ID format");
        }
        else if(!targetCurrencyID.matches("^\\d+$")){
            throw new InvalidExchangeRateFormatException("Invalid ID format");
        }
        else if(!rate.matches("^\\d+(\\.\\d+)?$")){
            throw new InvalidExchangeRateFormatException("Invalid rate format");
        }

        Currency baseCurrency = currencyDao.getById(Integer.parseInt(baseCurrencyID))
                .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));

        Currency targetCurrency = currencyDao.getById(Integer.parseInt(targetCurrencyID))
                .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));

        if(checkIfPresent(baseCurrency, targetCurrency)){
            throw new ExchangeRateAlreadyExistException("ExchangeRate already exists");
        }
    }

    private boolean checkIfPresent(Currency baseCurrency, Currency targetCurrency) throws SQLException {
        return exchangeRateDao.getByCodePair(baseCurrency.getCode(), targetCurrency.getCode()).isPresent();
    }
}