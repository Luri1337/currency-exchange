package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exception.ExceptionHandler;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.InvalidExchangeRateFormatException;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.util.AppMassages;
import org.example.currency_exchange.util.validation.ExchangeRateValidator;

import java.io.IOException;
import java.math.BigDecimal;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private static final ExchangeRateValidator validator = new ExchangeRateValidator();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        String baseCurrencyCode = path.substring(14, 17).toUpperCase();
        String targetCurrencyCode = path.substring(17, 20).toUpperCase();
        try {
            validator.validateRequest(req);
            ExchangeRate exchangeRate = exchangeRateDao.getByCodePair(baseCurrencyCode, targetCurrencyCode)
                    .orElseThrow(() -> new ExchangeRateNotFoundException(AppMassages.EXCHANGE_RATE_NOT_FOUND));
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(exchangeRate));
        } catch (MissingRequiredParameterException | InvalidExchangeRateFormatException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ExchangeRateNotFoundException | CurrencyNotFoundException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, AppMassages.INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        String baseCurrencyCode = path.substring(14, 17).toUpperCase();
        String targetCurrencyCode = path.substring(17).toUpperCase();
        String rate = req.getParameter("rate");

        try {
            validator.validateRequest(req);

            ExchangeRate exchangeRate = exchangeRateDao.getByCodePair(baseCurrencyCode, targetCurrencyCode)
                    .orElseThrow(() -> new ExchangeRateNotFoundException(AppMassages.EXCHANGE_RATE_NOT_FOUND));

            exchangeRate.setRate(new BigDecimal(rate));
            exchangeRateDao.update(exchangeRate);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(exchangeRate));
        } catch (MissingRequiredParameterException | InvalidExchangeRateFormatException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ExchangeRateNotFoundException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, AppMassages.INTERNAL_SERVER_ERROR);
        }
    }
}
