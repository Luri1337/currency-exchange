package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private static final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
           List<ExchangeRateDto> exchangeRates = exchangeRateService.getAllExchangeRates();
           resp.getWriter().write(new ObjectMapper().writeValueAsString(exchangeRates));
       }
       catch (Exception e){
           resp.getWriter().write(e.getMessage());
       }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyID = req.getParameter("baseCurrencyID");
        String targetCurrency = req.getParameter("targetCurrencyID");
        String rate = req.getParameter("rate");
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRate exchangeRate = new ExchangeRate(Integer.valueOf(baseCurrencyID), Integer.valueOf(targetCurrency), BigDecimal.valueOf(Double.valueOf(rate)));
            exchangeRateDao.create(exchangeRate);

            ExchangeRate addedExchangeRate = exchangeRateDao.getById(exchangeRate.getId())
                    .orElseThrow(() -> new RuntimeException("Exchange rate not found"));

            resp.getWriter().write(objectMapper.writeValueAsString(addedExchangeRate));
        }catch (Exception e){
            resp.getWriter().write(e.getMessage());
        }
    }
}
