package com.designtechnologies.challenge.core.documents;

import java.util.ArrayList;
import java.util.List;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Invoice extends AbstractDocument {

  private final List<AbstractDocument> notes = new ArrayList<>();

  public MonetaryAmount getTotal(CurrencyUnit currency, ExchangeRateProvider rateProvider) {
    CurrencyConversion conversion = rateProvider.getCurrencyConversion(currency);

    MonetaryAmount invoiceSum = notes.stream()
        .map(note -> conversion.apply(note.getAmount()))
        .reduce(conversion.apply(amount), MonetaryAmount::add);

    if (invoiceSum.isNegative()) {
      throw new RuntimeException("Invoice (%d) sum must greater or equal to 0".formatted(number));
    }

    return invoiceSum;
  }

  public void addNote(AbstractDocument note) {
    notes.add(note);
  }
}
