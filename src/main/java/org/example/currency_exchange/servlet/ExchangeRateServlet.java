package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exception.ExceptionHandler;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.InvalidExchangeRateFormatException;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.util.ExchangeRateValidator;

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
                    .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate not found"));
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(exchangeRate));
        } catch (Exception e) {
            if (e.getMessage().equals("Exchange rate not found")) {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found");
            } else if (e.getMessage().equals("Currency not found")) {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, "Currency not found");
            } else if (e.getMessage().equals("Required parameter is missing")) {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, "Required parameter is missing");
            } else if (e.getMessage().equals("Invalid code pair")) {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid code pair");
            } else {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
            }
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
                    .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate not found"));

            exchangeRate.setRate(new BigDecimal(rate));
            exchangeRateDao.update(exchangeRate);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(exchangeRate));
        } catch (MissingRequiredParameterException | InvalidExchangeRateFormatException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ExchangeRateNotFoundException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
