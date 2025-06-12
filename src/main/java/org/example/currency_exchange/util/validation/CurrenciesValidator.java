package org.example.currency_exchange.util.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.exception.MissingRequiredParameterException;
import org.example.currency_exchange.exception.currencyException.CurrencyAlreadyExistException;
import org.example.currency_exchange.exception.currencyException.InvalidCurrencyFormatException;
import org.example.currency_exchange.util.AppMassages;

public class CurrenciesValidator extends Validator {
    private final CurrencyDao currencyDao = new CurrencyDao();

    @Override
    public void validateRequest(HttpServletRequest request) {
        String name = request.getParameter("name").toUpperCase();
        String code = request.getParameter("code").toUpperCase();
        String sign = request.getParameter("sign");

        validateCurrency(name, code, sign);
    }

    private void validateCurrency(String name, String code, String sign) {
        if (name == null || name.isBlank() || code == null || code.isBlank() || sign == null || sign.isBlank()) {
            throw new MissingRequiredParameterException(AppMassages.MISSING_REQUIRED_PARAMETER);
        }

        if (!name.matches("[A-Z]+")) {
            throw new InvalidCurrencyFormatException(AppMassages.INVALID_CURRENCY_NAME_FORMAT);
        }
        if (!code.matches("^[A-Z]{3}$")) {
            throw new InvalidCurrencyFormatException(AppMassages.INVALID_CURRENCY_CODE_FORMAT);
        }
        if (!sign.matches("[\\p{Sc}]")) {
            throw new InvalidCurrencyFormatException(AppMassages.INVALID_CURRENCY_SIGN_FORMAT);
        }
        if (currencyDao.getByCode(code).isPresent()) {
            throw new CurrencyAlreadyExistException(AppMassages.CURRENCY_ALREADY_EXISTS);
        }
    }
}
