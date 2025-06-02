package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exception.*;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.util.ExchangeRatesValidator;
import org.example.currency_exchange.util.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private static final CurrencyDao currencyDao = new CurrencyDao();
    private static final Validator validator = new ExchangeRatesValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            List<ExchangeRate> exchangeRates = exchangeRateDao.getAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(exchangeRates));
       }
       catch (Exception e){
           ExceptionHandler.handleException(resp, 500, "Internal Server Error");
       }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyID = req.getParameter("baseCurrencyID");
        String targetCurrencyID = req.getParameter("targetCurrencyID");
        String rate = req.getParameter("rate");
        try{
            validator.validateRequest(req);
            ObjectMapper objectMapper = new ObjectMapper();
            Currency baseCurrency = currencyDao.getById(Integer.parseInt(baseCurrencyID))
                    .orElseThrow(() ->  new CurrencyNotFoundException("Currency not found"));

            Currency targetCurrency = currencyDao.getById(Integer.parseInt(targetCurrencyID))
                    .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));

            ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, BigDecimal.valueOf(Double.valueOf(rate)));
            exchangeRateDao.create(exchangeRate);

            ExchangeRate addedExchangeRate = exchangeRateDao.getById(exchangeRate.getId())
                    .orElseThrow(() -> new RuntimeException("Exchange rate not found"));

            resp.setStatus(201);
            resp.getWriter().write(objectMapper.writeValueAsString(addedExchangeRate));
        }catch (CurrencyNotFoundException e){
            ExceptionHandler.handleException(resp, 404, e.getMessage());
        }catch (MissingRequiredParameterException | InvalidExchangeRateFormatException e) {
         ExceptionHandler.handleException(resp, 400, e.getMessage());
        }catch (ExchangeRateAlreadyExistException e) {
            ExceptionHandler.handleException(resp, 409, e.getMessage());
        }catch (Exception e){
            ExceptionHandler.handleException(resp, 500, e.getMessage());
        }
    }
}
