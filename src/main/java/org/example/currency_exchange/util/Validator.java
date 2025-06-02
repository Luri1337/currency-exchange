package org.example.currency_exchange.util;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
// todo: сделать нормальную оопешную валидацию а не эту хуйню, где для каждого сервлета свой валидатор
public abstract class Validator {
    public abstract void validateRequest(HttpServletRequest request) throws SQLException;
}
