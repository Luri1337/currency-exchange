package org.example.currency_exchange.dao;

import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements CrudDao<ExchangeRate> {
    private static final CurrencyDao currencyDao = new CurrencyDao();

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) {
        String query = "INSERT INTO exchangeRates(basecurrencyid, targetcurrencyid, rate) VALUES(?, ?, ?)";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            ps.setInt(1, exchangeRate.getBaseCurrencyID());
            ps.setInt(2, exchangeRate.getTargetCurrencyID());
            ps.setBigDecimal(3, exchangeRate.getRate());

            int affectedRows = ps.executeUpdate();
            System.out.println("Connecting to DB: " + conn.getMetaData().getURL());
            conn.commit();

            if (affectedRows == 0) {
                throw new SQLException("Creating ExchangeRate failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    exchangeRate.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating ExchangeRate failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRate;

    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public List<ExchangeRate> getAll() {
        String query = "select * from exchangerates";

        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection conn = DataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                exchangeRates.add(getExchangeRate(rs));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ExchangeRate> getById(int id) {
        String query = "SELECT * FROM ExchangeRates WHERE id = ?";
        Optional<ExchangeRate> exchangeRate = Optional.empty();

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    exchangeRate = Optional.of(getExchangeRate(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRate;
    }

    public Optional<ExchangeRate> getByCodePair(String base, String target) throws SQLException {
        String query = "select * from exchangerates where basecurrencyid = ? and targetcurrencyid = ?";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            Currency baseCurrency = currencyDao.getByCode(base)
                    .orElseThrow(() -> new RuntimeException("Base currency not found"));

            Currency targetCurrency = currencyDao.getByCode(target)
                    .orElseThrow(() -> new RuntimeException("Target currency not found"));

            ps.setInt(1, baseCurrency.getId());
            ps.setInt(2, targetCurrency.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(getExchangeRate(rs));
            }
            return Optional.empty();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate getExchangeRate(ResultSet rs) throws SQLException {
        return new ExchangeRate(rs.getInt("id"),
                rs.getInt("baseCurrencyId"),
                rs.getInt("targetCurrencyId"),
                rs.getBigDecimal("rate"));
    }
}
