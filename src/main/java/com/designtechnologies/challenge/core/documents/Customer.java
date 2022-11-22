package com.designtechnologies.challenge.core.documents;

import java.util.LinkedList;
import java.util.List;
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

  private final List<Invoice> invoices = new LinkedList<>();
  private String name;
  private String vatNumber;

  public void addInvoice(Invoice invoice) {
    invoices.add(invoice);
  }

  public MonetaryAmount calculateTotal(CurrencyUnit currency, ExchangeRateProvider rateProvider) {
    CurrencyConversion conversion = rateProvider.getCurrencyConversion(currency);

    return invoices.stream()
        .map(invoice -> conversion.apply(invoice.getTotal()))
        .reduce(Money.of(0, currency), MonetaryAmount::add);
  }
}
