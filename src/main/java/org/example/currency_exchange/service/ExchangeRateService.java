package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.model.Currency;
import org.example.currency_exchange.model.ExchangeRate;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRateService {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public ExchangeRateDto convertToDto(ExchangeRate exchangeRate) {
        Currency baseCurrency = currencyDao.getById(exchangeRate.getBaseCurrencyID())
                .orElseThrow(() -> new RuntimeException("Base currency not found"));
        Currency targetCurrency = currencyDao.getById(exchangeRate.getTargetCurrencyID())
                .orElseThrow(() -> new RuntimeException("Target currency not found"));

        return new ExchangeRateDto(
                exchangeRate.getId(),
                baseCurrency,
                targetCurrency,
                exchangeRate.getRate()
        );
    }

    public List<ExchangeRateDto> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateDao.getAll();

        return exchangeRates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ExchangeRateDto getExchangeRate(String base, String target) throws SQLException {
        ExchangeRate exchangeRate = exchangeRateDao.getByCodePair(base, target)
                .orElseThrow(() -> new RuntimeException("Base and target not found"));
        return convertToDto(exchangeRate);
    }
}
