package com.designtechnologies.challenge.core.documents;

import java.util.LinkedList;
import java.util.List;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import lombok.Builder;
import lombok.Data;
import org.javamoney.moneta.Money;

@Data
@Builder
public class Customer {

  private String name;
  private String vatNumber;
  private final List<Invoice> invoices = new LinkedList<>();

  public void addInvoice(Invoice invoice) {
    invoices.add(invoice);
  }

  public MonetaryAmount calculateTotal() {
    return invoices.stream()
        .reduce(
            Money.of(0, Monetary.getCurrency("USD")),
            (acc, invoice) -> acc.add(invoice.getTotal()),
            MonetaryAmount::add
        );
  }
}
