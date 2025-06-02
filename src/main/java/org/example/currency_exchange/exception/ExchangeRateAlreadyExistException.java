package org.example.currency_exchange.exception;

public class ExchangeRateAlreadyExistException extends RuntimeException {
    public ExchangeRateAlreadyExistException(String message) {
        super(message);
    }
}
