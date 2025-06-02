package org.example.currency_exchange.dao;

import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao implements CrudDao<Currency> {
    @Override
    public Currency create(Currency currency) {
        String query = "INSERT INTO currencies (code, fullname, sign) VALUES (?, ?, ?)";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());

            int affectedRows = ps.executeUpdate();
            System.out.println("Connecting to DB: " + conn.getMetaData().getURL());
            conn.commit();

            if (affectedRows == 0) {
                throw new SQLException("Creating currency failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    currency.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating currency failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return currency;
    }


    @Override
    public Currency update(Currency currency) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Currency> getById(int id) {
        String query = "SELECT * FROM Currencies WHERE id = ?";
        Optional<Currency> currency = Optional.empty();

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currency = Optional.of(getCurrency(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }



    public Optional<Currency> getByCode(String code) {
        String query = "SELECT * FROM Currencies WHERE code = ?";
        Optional<Currency> currency = Optional.empty();

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currency = Optional.of(getCurrency(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }

    private Currency getCurrency(ResultSet rs) throws SQLException {
        return new Currency(
                rs.getInt("id"),
                rs.getString("fullName"),
                rs.getString("code"),
                rs.getString("sign")
        );
    }
}

