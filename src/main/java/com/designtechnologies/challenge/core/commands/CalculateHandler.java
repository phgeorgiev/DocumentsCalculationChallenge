package com.designtechnologies.challenge.core.commands;

import com.designtechnologies.challenge.core.documents.Customer;
import com.designtechnologies.challenge.core.documents.CustomerRegistry;
import com.designtechnologies.challenge.core.documents.Invoice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.money.Monetary;
import org.javamoney.moneta.Money;

public class CalculateHandler {

  private final CustomerRegistry customerRegistry;

  public CalculateHandler(CustomerRegistry customerRegistry) {
    this.customerRegistry = customerRegistry;
  }

  public CalculateResponse handle(CalculateRequest request) {
    return createResponse(
        Arrays.stream(request.getDocuments())
            .map(this::createCustomer)
            .collect(Collectors.toCollection(ArrayList::new))
    );
  }

  private Customer createCustomer(DocumentRequest document) {
    Customer customer = customerRegistry.getCustomer(document.getCustomer())
        .orElseGet(() -> Customer.builder()
            .name(document.getCustomer())
            .vatNumber(document.getVatNumber())
            .build());

    customerRegistry.addCustomer(customer);
    customer.addInvoice(createInvoice(document));
    return customer;
  }

  private Invoice createInvoice(DocumentRequest document) {
    return Invoice.builder()
        .total(Money.of(document.getTotal(), Monetary.getCurrency(document.getCurrency())))
        .build();
  }

  private CalculateResponse createResponse(List<Customer> customers) {
    return CalculateResponse.builder()
        .customers(
            customers.stream()
                .map(customer -> CustomerResponse.builder()
                    .name(customer.getName())
                    .balance(
                        Monetary.getDefaultRounding()
                            .apply(customer.calculateTotal())
                            .getNumber()
                            .doubleValue())
                    .build())
                .toArray(CustomerResponse[]::new)
        )
        .build();
  }
}
