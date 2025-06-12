package org.example.currency_exchange.util.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.exchangeRateException.InvalidExchangeRateFormatException;
import org.example.currency_exchange.util.AppMassages;

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

        if (param.isBlank()) {
            throw new MissingRequiredParameterException(AppMassages.MISSING_REQUIRED_PARAMETER);
        }
        return param;
    }

    private void validateCodePair(String codePair) {
        if (!codePair.matches("^[A-Z]{6}$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_CODE_PAIR);
        }
    }

    private void validateRate(String rate) {
        if (rate.isBlank()) {
            throw new MissingRequiredParameterException(AppMassages.MISSING_REQUIRED_PARAMETER);
        }
        if (!rate.matches("^\\+?\\d+(\\.\\d+)?$")) {
            throw new InvalidExchangeRateFormatException(AppMassages.INVALID_RATE_FORMAT);
        }
    }
}
