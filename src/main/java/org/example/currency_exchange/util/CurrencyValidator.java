package org.example.currency_exchange.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.exception.currencyException.InvalidCurrencyFormatException;
import org.example.currency_exchange.exception.MissingRequiredParameterException;

public class CurrencyValidator extends Validator {

    @Override
    public void validateRequest(HttpServletRequest request) {
        String param = getRequiredParameter(request);
        validateCurrencyCode(param);
    }

    private String getRequiredParameter(HttpServletRequest request) {
        String param = request.getPathInfo().substring(1).toUpperCase();

        if(param.isBlank()) {
            throw new MissingRequiredParameterException("Required parameter is missing");
        }
        return param;
    }

    private void validateCurrencyCode(String param) {
        if (!param.matches("^[A-Z]{3}$")) {
            throw new InvalidCurrencyFormatException("Invalid currency code format");
        }
    }


}
