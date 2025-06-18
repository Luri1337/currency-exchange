package org.example.currency_exchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeDto;
import org.example.currency_exchange.exception.ExceptionHandler;
import org.example.currency_exchange.exception.InvalidExchangeFormatException;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.service.ExchangeService;
import org.example.currency_exchange.util.AppMassages;
import org.example.currency_exchange.util.validation.ExchangeValidator;
import org.example.currency_exchange.util.validation.Validator;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private static final ExchangeService exchangeService = new ExchangeService();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Validator validator = new ExchangeValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        try {
            validator.validateRequest(req);
            ExchangeDto exchange = exchangeService.exchange(from, to, amount);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(exchange));
        } catch (MissingRequiredParameterException | InvalidExchangeFormatException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyNotFoundException e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            ExceptionHandler.handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, AppMassages.INTERNAL_SERVER_ERROR);
        }
    }


}
