package org.example.currency_exchange.utils;

import jakarta.servlet.http.HttpServletRequest;

public abstract class Validator {
    public abstract void validateRequest(HttpServletRequest request);
}
