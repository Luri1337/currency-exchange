package org.example.currency_exchange.exceptions;

public class ExchangeRateAlreadyExistException extends RuntimeException {
    public ExchangeRateAlreadyExistException(String message) {
        super(message);
    }
}
