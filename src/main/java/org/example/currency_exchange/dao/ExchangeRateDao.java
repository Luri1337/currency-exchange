package org.example.currency_exchange.dao;

import org.example.currency_exchange.model.ExchangeRate;

import java.util.List;
import java.util.Optional;

public class ExchangeRateDao implements CrudDao<ExchangeRate> {
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
        return List.of();
    }

    @Override
    public Optional<ExchangeRate> getById(int id) {
        return Optional.empty();
    }
}
