package org.example.currency_exchange.servlets;

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
import java.util.List;
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
    //TODO     доделать реализацию этого метода, адаптировать из формата x-www-form-urlencoded под формат json
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Scanner scanner = new Scanner(req.getInputStream(), "UTF-8");
        String jsonData = scanner.useDelimiter("\\A").next();
        scanner.close();

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRate exchangeRate = objectMapper.readValue(jsonData, ExchangeRate.class);
            exchangeRateDao.create(exchangeRate);

            ExchangeRate addedExchangeRate = exchangeRateDao.getById(exchangeRate.getId())
                    .orElseThrow(() -> new RuntimeException("Exchange rate not found"));

            resp.getWriter().write(objectMapper.writeValueAsString(addedExchangeRate));
        }catch (Exception e){
            resp.getWriter().write(e.getMessage());
        }
    }
}
