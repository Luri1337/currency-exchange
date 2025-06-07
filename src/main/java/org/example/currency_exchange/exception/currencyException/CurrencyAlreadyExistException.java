package org.example.currency_exchange.exception.currencyException;

public class CurrencyAlreadyExistException extends RuntimeException {
    public CurrencyAlreadyExistException(String message) {
        super(message);
    }
}
