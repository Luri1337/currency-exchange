package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.exceptions.CurrencyNotFoundException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.exceptions.ExceptionHandler;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyDao currencyDao = new CurrencyDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("aplication/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        try{
            String currencyCode = pathInfo.substring(1).toUpperCase();
            Currency currency = currencyDao.getByCode(currencyCode)
                    .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currency));
        }catch (Exception e){
            if(e.getMessage().equals("Currency not found")){
                ExceptionHandler.handleException(resp, 404, "Currency not found");
            }
            if (e.getMessage().equals("Currency not found")){}

        }
    }
}
