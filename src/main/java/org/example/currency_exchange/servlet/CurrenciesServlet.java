package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.exception.ExceptionHandler;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.CurrenciesValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final CurrenciesValidator currenciesValidator = new CurrenciesValidator();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        try {
            currenciesValidator.validateRequest(req);
            Currency currency = new Currency(name, code, sign);
            currencyDao.create(currency);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currency));

        } catch (Exception e) {
            if (e.getMessage().equals("Missing required currency field")) {
                ExceptionHandler.handleException(resp, 400, "Missing required currency field");
            } else if (e.getMessage().equals("Invalid currency name format")) {
                ExceptionHandler.handleException(resp, 400, "Invalid currency name format");
            } else if (e.getMessage().equals("Invalid currency code format")) {
                ExceptionHandler.handleException(resp, 400, "Invalid currency code format");
            } else if (e.getMessage().equals("Invalid currency sign format")) {
                ExceptionHandler.handleException(resp, 400, "Invalid currency sign format");
            } else if (e.getMessage().equals("Currency already exists")) {
                ExceptionHandler.handleException(resp, 409, "Currency already exists");
            } else {
                ExceptionHandler.handleException(resp, 500, "Internal Server Error");
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> currencies = currencyDao.getAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currencies));
        } catch (SQLException e) {
            resp.getWriter().write(e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
