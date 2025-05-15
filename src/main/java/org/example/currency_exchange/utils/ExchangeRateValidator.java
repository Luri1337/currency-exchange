package org.example.currency_exchange.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.exceptions.InvalidExchangeRateFormatException;
import org.example.currency_exchange.exceptions.MissingRequiredParameterException;

public class ExchangeRateValidator extends Validator {
    @Override
    public void validateRequest(HttpServletRequest request) {
        String codePair = getRequiredParameter(request);
        validateCodePair(codePair);
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
}
