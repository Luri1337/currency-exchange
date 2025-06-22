package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exception.*;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateAlreadyExistException;
import org.example.currency_exchange.exception.exchangeRateException.InvalidExchangeRateFormatException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.util.AppMassages;
import org.example.currency_exchange.util.validation.ExchangeRatesValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private static final CurrencyDao currencyDao = new CurrencyDao();
    private static final ExchangeRatesValidator validator = new ExchangeRatesValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRate> exchangeRates = exchangeRateDao.getAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(exchangeRates));
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, 500, AppMassages.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyID = req.getParameter("baseCurrencyCode").toUpperCase();
        String targetCurrencyID = req.getParameter("targetCurrencyCode").toUpperCase();
        String rate = req.getParameter("rate");
        try {
            validator.validateRequest(baseCurrencyID, targetCurrencyID, rate);
            ObjectMapper objectMapper = new ObjectMapper();
            Currency baseCurrency = currencyDao.getByCode(baseCurrencyID)
                    .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

            Currency targetCurrency = currencyDao.getByCode(targetCurrencyID)
                    .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

            ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, BigDecimal.valueOf(Double.parseDouble(rate)));
            exchangeRateDao.create(exchangeRate);

            ExchangeRate addedExchangeRate = exchangeRateDao.getById(exchangeRate.getId())
                    .orElseThrow(() -> new RuntimeException(AppMassages.EXCHANGE_RATE_NOT_FOUND));

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(addedExchangeRate));
        } catch (CurrencyNotFoundException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (MissingRequiredParameterException | InvalidExchangeRateFormatException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ExchangeRateAlreadyExistException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
