package com.designtechnologies.challenge.core.documents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ProviderContext;
import javax.money.convert.RateType;
import org.javamoney.moneta.convert.ExchangeRateBuilder;
import org.javamoney.moneta.spi.AbstractRateProvider;
import org.javamoney.moneta.spi.DefaultNumberValue;

public class CustomExchangeRateProvider extends AbstractRateProvider {

  private final Map<String, Double> rates = new HashMap<>();

  public CustomExchangeRateProvider() {
    super(ProviderContext.of("custom"));
  }

  @Override
  public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
    CurrencyUnit baseCurrency = conversionQuery.getBaseCurrency();
    return new ExchangeRateBuilder(getContext().getProviderName(), RateType.ANY)
        .setBase(baseCurrency)
        .setTerm(conversionQuery.getCurrency())
        .setFactor(DefaultNumberValue.of(1 / rates.get(baseCurrency.getCurrencyCode())))
        .build();
  }

  public void setRates(String exchangeRates) {
    Arrays.stream(exchangeRates.split(","))
        .map(s -> s.split(":"))
        .forEach(s -> rates.put(s[0], Double.parseDouble(s[1])));

    if (!rates.containsValue(1.0)) {
      throw new RuntimeException("Default currency must be specified by giving it an exchange rate of 1");
    }
  }
}
