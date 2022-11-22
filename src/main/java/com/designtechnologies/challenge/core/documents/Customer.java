package com.designtechnologies.challenge.core.documents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import lombok.Builder;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
@Builder
public class Customer {

  private final Map<Integer, Invoice> invoices = new HashMap<>();
  private String name;
  private String vatNumber;

  public void addInvoice(Invoice invoice) {
    invoices.put(invoice.getNumber(), invoice);
  }

  public Optional<Invoice> getInvoice(int number) {
    return Optional.ofNullable(invoices.getOrDefault(number, null));
  }

  public MonetaryAmount calculateTotal(CurrencyUnit currency, ExchangeRateProvider rateProvider) {
    CurrencyConversion conversion = rateProvider.getCurrencyConversion(currency);

    return new ArrayList<>(invoices.values()).stream()
        .map(invoice -> conversion.apply(invoice.getTotal(currency, rateProvider)))
        .reduce(Money.of(0, currency), MonetaryAmount::add);
  }
}
