package org.example.currency_exchange.exceptions;

public class CurrencyAlreadyExistException extends RuntimeException {
    public CurrencyAlreadyExistException(String message) {
        super(message);
    }
}
