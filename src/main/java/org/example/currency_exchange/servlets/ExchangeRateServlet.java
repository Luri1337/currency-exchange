package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.exceptions.ExchangeRateNotFoundException;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.service.ExchangeRateService;
import org.example.currency_exchange.exceptions.ExceptionHandler;
import org.example.currency_exchange.utils.ExchangeRateValidator;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    ExchangeRateValidator validator = new ExchangeRateValidator();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        String baseCurrencyCode = path.substring(14, 17).toUpperCase();
        String targetCurrencyCode = path.substring(17, 20).toUpperCase();
        try {
            validator.validateRequest(req);
            ExchangeRate exchangeRate = exchangeRateDao.getByCodePair(baseCurrencyCode, targetCurrencyCode)
                    .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate not found") );
            ObjectMapper mapper = new ObjectMapper();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(exchangeRate));
        } catch (Exception e) {
            if(e.getMessage().equals("Exchange rate not found")) {
                ExceptionHandler.handleException(resp, 404, "Exchange rate not found");
            }
            else if(e.getMessage().equals("Currency not found")) {
                ExceptionHandler.handleException(resp, 404, "Currency not found");
            }
            else if(e.getMessage().equals("Required parameter is missing")){
                ExceptionHandler.handleException(resp, 400, "Required parameter is missing");
            }
            else if(e.getMessage().equals("Invalid code pair")) {
                ExceptionHandler.handleException(resp, 400, "Invalid code pair");
            }
            else {
                ExceptionHandler.handleException(resp, 500, "Internal Server Error");
            }
        }


    }
}
