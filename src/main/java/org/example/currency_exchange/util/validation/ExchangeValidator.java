package org.example.currency_exchange.util.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.exception.InvalidExchangeFormatException;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.util.AppMassages;

import java.sql.SQLException;


public class ExchangeValidator extends Validator {
    @Override
    public void validateRequest(HttpServletRequest request) throws SQLException {
        String from = request.getParameter("from").toUpperCase();
        String to = request.getParameter("to").toUpperCase();
        String amount = request.getParameter("amount");

        validate(from, to, amount);

    }

    private void validate(String from, String to, String amount) throws SQLException {
        if (from.isBlank() || to.isBlank() || amount.isBlank()) {
            throw new MissingRequiredParameterException(AppMassages.MISSING_REQUIRED_PARAMETER);
        }
        if (!from.matches("^[A-Z]{3}$")) {
            throw new InvalidExchangeFormatException(AppMassages.INVALID_CURRENCY_CODE_FORMAT);
        }
        if (!to.matches("^[A-Z]{3}$")) {
            throw new InvalidExchangeFormatException(AppMassages.INVALID_CURRENCY_CODE_FORMAT);
        }
        if (!amount.matches("^\\d+(\\.\\d+)?$")) {
            throw new InvalidExchangeFormatException(AppMassages.INVALID_AMOUNT_FORMAT);
        }
    }
}
