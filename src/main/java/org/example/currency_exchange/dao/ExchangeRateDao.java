package org.example.currency_exchange.dao;

import org.example.currency_exchange.exception.currencyException.CurrencyNotFoundException;
import org.example.currency_exchange.exception.exchangeRateException.ExchangeRateNotFoundException;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.ExchangeRate;
import org.example.currency_exchange.util.AppMassages;
import org.example.currency_exchange.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements CrudDao<ExchangeRate> {
    private static final CurrencyDao currencyDao = new CurrencyDao();

    @Override
    public void create(ExchangeRate exchangeRate) {
        String query = "INSERT INTO exchangeRates(basecurrencyid, targetcurrencyid, rate) VALUES(?, ?, ?)";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            ps.setInt(1, exchangeRate.getBaseCurrency().getId());
            ps.setInt(2, exchangeRate.getTargetCurrency().getId());
            ps.setBigDecimal(3, exchangeRate.getRate());

            int affectedRows = ps.executeUpdate();
            System.out.println("Connecting to DB: " + conn.getMetaData().getURL());
            conn.commit();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException(AppMassages.CREATING_EXCHANGE_RATE_FAilED);
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    exchangeRate.setId(rs.getInt(1));
                } else {
                    conn.rollback();
                    throw new SQLException(AppMassages.CREATING_EXCHANGE_RATE_FAilED);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(ExchangeRate exchangeRate) throws SQLException {
        String query = "update exchangeRates set rate = ? where id = ?";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setBigDecimal(1, exchangeRate.getRate());
            ps.setInt(2, exchangeRate.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public Optional<ExchangeRate> getByCodePair(String base, String target) throws SQLException, CurrencyNotFoundException {
        String query = "select * from exchangerates where basecurrencyid = ? and targetcurrencyid = ?";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            Currency baseCurrency = currencyDao.getByCode(base)
                    .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

            Currency targetCurrency = currencyDao.getByCode(target)
                    .orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND));

            ps.setInt(1, baseCurrency.getId());
            ps.setInt(2, targetCurrency.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(getExchangeRate(rs));
            }
            return Optional.empty();
        } catch (CurrencyNotFoundException e) {
            throw new ExchangeRateNotFoundException(AppMassages.EXCHANGE_RATE_NOT_FOUND);
        } catch (Exception e) {
            throw e;
        }
    }

    private ExchangeRate getExchangeRate(ResultSet rs) throws SQLException {
        return new ExchangeRate(rs.getInt("id"),
                currencyDao.getById(rs.getInt("baseCurrencyId")).orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND)),
                currencyDao.getById(rs.getInt("targetCurrencyId")).orElseThrow(() -> new CurrencyNotFoundException(AppMassages.CURRENCY_NOT_FOUND)),
                rs.getBigDecimal("rate"));
    }
}
