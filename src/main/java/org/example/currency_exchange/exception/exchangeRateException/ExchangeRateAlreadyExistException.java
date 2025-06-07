package org.example.currency_exchange.exception.exchangeRateException;

public class ExchangeRateAlreadyExistException extends RuntimeException {
    public ExchangeRateAlreadyExistException(String message) {
        super(message);
    }
}
