package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.exception.ExceptionHandler;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.currencyException.InvalidCurrencyFormatException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.AppMassages;
import org.example.currency_exchange.util.validation.CurrencyValidator;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyDao currencyDao = new CurrencyDao();
    CurrencyValidator validator = new CurrencyValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyCode = req.getPathInfo().substring(1).toUpperCase();

        try {
            validator.validateRequest(currencyCode);
            Currency currency = currencyDao.getByCode(currencyCode)
                    .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currency));
        } catch (CurrencyNotFoundException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (MissingRequiredParameterException | InvalidCurrencyFormatException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, AppMassages.INTERNAL_SERVER_ERROR);
        }
    }
}