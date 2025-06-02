package org.example.currency_exchange.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.exception.InvalidExchangeRateFormatException;
import org.example.currency_exchange.exception.MissingRequiredParameterException;

public class ExchangeRateValidator extends Validator {
    @Override
    public void validateRequest(HttpServletRequest request) {
        String codePair = getRequiredParameter(request);
        validateCodePair(codePair);

        if (request.getMethod().equalsIgnoreCase("PATCH")) {
            validateRate(request.getParameter("rate"));
        }

    }

    private String getRequiredParameter(HttpServletRequest request) {
        String param = request.getPathInfo().substring(1).toUpperCase();

        if(param.isBlank()) {
            throw new MissingRequiredParameterException("Required parameter is missing");
        }
        return param;
    }

    private void validateCodePair(String codePair) {
        if(!codePair.matches("^[A-Z]{6}$")) {
            throw new InvalidExchangeRateFormatException("Invalid code pair");
        }
    }

    private void validateRate(String rate) {
        if (rate.isBlank()) {
            throw new MissingRequiredParameterException("Rate is missing");
        }
        else if (!rate.matches("^\\+?\\d+(\\.\\d+)?$")) {
            throw new InvalidExchangeRateFormatException("Invalid rate format");
        }
    }
}
