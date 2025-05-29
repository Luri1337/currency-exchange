package org.example.currency_exchange.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.service.ExchangeService;

import java.io.IOException;
import java.sql.SQLException;

public class ExchangeServlet extends HttpServlet {
    private static final ExchangeService exchangeService = new ExchangeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        try{

            exchangeService.exchange(from, to, amount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
