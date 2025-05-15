package org.example.currency_exchange.exceptions;

public class MissingRequiredParameterException extends RuntimeException {
    public MissingRequiredParameterException(String message) {
        super(message);
    }
}
