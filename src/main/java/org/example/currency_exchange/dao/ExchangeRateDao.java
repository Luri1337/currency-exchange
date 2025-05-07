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
        return null;
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
        return Optional.empty();
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
