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
import org.example.currency_exchange.utils.CurrencyValidator;
import org.example.currency_exchange.utils.Validator;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyDao currencyDao = new CurrencyDao();
    Validator validator = new CurrencyValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("aplication/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        try{
            validator.validateRequest(req);
            String currencyCode = pathInfo.substring(1).toUpperCase();
            Currency currency = currencyDao.getByCode(currencyCode)
                    .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));
            resp.getWriter().write(new ObjectMapper().writeValueAsString(currency));
        }catch (Exception e){
            if(e.getMessage().equals("Currency not found")){
                ExceptionHandler.handleException(resp, 404, "Currency not found");
            }
            else if (e.getMessage().equals("Required parameter is missing")){
                ExceptionHandler.handleException(resp, 400, "Required parameter is missing");
            }
            else if (e.getMessage().equals("Invalid currency code format")){
                ExceptionHandler.handleException(resp, 400, "Invalid currency code format");
            }
            else{
                ExceptionHandler.handleException(resp, 500, "Internal Server Error");
            }
        }
    }
}
