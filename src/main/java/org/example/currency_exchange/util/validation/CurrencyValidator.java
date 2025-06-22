package org.example.currency_exchange.util.validation;

import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.InvalidCurrencyFormatException;
import org.example.currency_exchange.util.AppMassages;

public class CurrencyValidator {

    public void validateRequest(String currencyCode) {
        if (currencyCode.isBlank()) {
            throw new MissingRequiredParameterException(AppMassages.MISSING_REQUIRED_PARAMETER);
        }
        if (!currencyCode.matches("^[A-Z]{3}$")) {
            throw new InvalidCurrencyFormatException(AppMassages.INVALID_CURRENCY_CODE_FORMAT);
        }
    }
}
