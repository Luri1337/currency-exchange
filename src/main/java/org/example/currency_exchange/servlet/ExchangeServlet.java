package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeDto;
import org.example.currency_exchange.service.ExchangeService;
import org.example.currency_exchange.util.validation.ExchangeRatesValidator;
import org.example.currency_exchange.util.validation.Validator;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private static final ExchangeService exchangeService = new ExchangeService();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Validator validator = new ExchangeRatesValidator(); 

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        try{

            ExchangeDto exchange = exchangeService.exchange(from, to, amount);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(exchange));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
