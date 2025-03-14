package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.model.Currency;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyDao currencyDao = new CurrencyDao();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Scanner scanner = new Scanner(req.getInputStream(), "UTF-8");
        String jsonData = scanner.useDelimiter("\\A").next();
        scanner.close();

        try{
            ObjectMapper mapper = new ObjectMapper();
            Currency currency = mapper.readValue(jsonData, Currency.class);
            currencyDao.create(currency);

            Currency addedCurrency = currencyDao.getByCode(currency.getCode())
                    .orElseThrow(() -> new RuntimeException("Currency not found"));
            resp.getWriter().write(new ObjectMapper().writeValueAsString(addedCurrency));
        }catch (Exception e){
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> currencies = currencyDao.getAll();
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currencies));
        } catch (SQLException e) {
            resp.getWriter().write(e.getMessage());
        }
    }
}
