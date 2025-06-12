package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.exception.ExceptionHandler;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.CurrencyAlreadyExistException;
import org.example.currency_exchange.exception.currencyException.InvalidCurrencyFormatException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.AppMassages;
import org.example.currency_exchange.util.validation.CurrenciesValidator;

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

        }catch (MissingRequiredParameterException | InvalidCurrencyFormatException e){
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        catch (CurrencyAlreadyExistException e){
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
        catch (Exception e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, AppMassages.INTERNAL_SERVER_ERROR);
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
