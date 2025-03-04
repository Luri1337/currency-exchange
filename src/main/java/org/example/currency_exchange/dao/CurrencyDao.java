package org.example.currency_exchange.dao;

import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.utils.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.*;

import static org.example.currency_exchange.utils.DataSource.getConnection;

public class CurrencyDao implements CrudDao<Currency> {
    @Override
    public Currency create(Currency currency) {
        return null;
    }

    @Override
    public Currency update(Currency currency) {
        return null;
    }

    @Override
    public Currency delete(Currency currency) {
        return null;
    }

    @Override
    public List<Currency> getAll() throws SQLException {
        String query = "SELECT * FROM Currencies";

        try (Connection conn = DataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            List<Currency> currencies = new ArrayList<>();
            while (rs.next()) {
                currencies.add(getCurrency(rs));
            }

            return currencies;
        }
    }


    private Currency getCurrency(ResultSet rs) throws SQLException {
        return new Currency(
                rs.getInt("id"),
                rs.getString("fullName"),
                rs.getString("code"),
                rs.getString("sign")
        );
    }


    @Override
    public Optional<Currency> getById(int id) {
        return Optional.empty();
    }
}
