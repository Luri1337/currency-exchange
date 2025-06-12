package org.example.currency_exchange.util.validation;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;

public abstract class Validator {
    public abstract void validateRequest(HttpServletRequest request) throws SQLException;
}
