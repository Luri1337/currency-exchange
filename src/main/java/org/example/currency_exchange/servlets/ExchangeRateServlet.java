package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.service.ExchangeRateService;
import org.example.currency_exchange.exceptions.ExceptionHandler;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateService exchangeRateService = new ExchangeRateService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        String baseCurrencyCode = path.substring(14, 17).toUpperCase();
        String targetCurrencyCode = path.substring(17, 20).toUpperCase();
        try {
            ExchangeRateDto exchangeRateDto = exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(), exchangeRateDto);
        } catch (SQLException e) {

        }


    }
}
