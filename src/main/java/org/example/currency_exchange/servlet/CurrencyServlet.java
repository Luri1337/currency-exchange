package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.ExceptionHandler;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.CurrencyValidator;
import org.example.currency_exchange.util.Validator;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyDao currencyDao = new CurrencyDao();
    Validator validator = new CurrencyValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            validator.validateRequest(req);
            String currencyCode = pathInfo.substring(1).toUpperCase();
            Currency currency = currencyDao.getByCode(currencyCode)
                    .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currency));
        } catch (Exception e) {
            if (e.getMessage().equals("Currency not found")) {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, "Currency not found");
            } else if (e.getMessage().equals("Required parameter is missing")) {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, "Required parameter is missing");
            } else if (e.getMessage().equals("Invalid currency code format")) {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code format");
            } else {
                ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
            }
        }
    }
}