package org.example.currency_exchange.exception;

public class MissingRequiredParameterException extends RuntimeException {
    public MissingRequiredParameterException(String message) {
        super(message);
    }
}
