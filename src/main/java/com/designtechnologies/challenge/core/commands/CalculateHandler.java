package com.designtechnologies.challenge.core.commands;

import com.designtechnologies.challenge.core.documents.CustomExchangeRateProvider;
import com.designtechnologies.challenge.core.documents.Customer;
import com.designtechnologies.challenge.core.documents.CustomerRegistry;
import com.designtechnologies.challenge.core.documents.Invoice;
import java.util.Arrays;
import java.util.List;
import javax.money.Monetary;
import org.javamoney.moneta.Money;

public class CalculateHandler {

  private final CustomerRegistry customerRegistry;
  private final CustomExchangeRateProvider rateProvider;

  public CalculateHandler(CustomerRegistry customerRegistry, CustomExchangeRateProvider rateProvider) {
    this.customerRegistry = customerRegistry;
    this.rateProvider = rateProvider;
  }

  public CalculateResponse handle(CalculateRequest request) {
    rateProvider.setRates(request.getExchangeRates());

    Arrays.stream(request.getDocuments())
        .map(this::createCustomer)
        .forEach(customerRegistry::addCustomer);

    return createResponse(request.getOutputCurrency(), customerRegistry.getAll());
  }

  private Customer createCustomer(DocumentRequest document) {
    Customer customer = customerRegistry.getCustomer(document.getCustomer())
        .orElseGet(() -> Customer.builder()
            .name(document.getCustomer())
            .vatNumber(document.getVatNumber())
            .build());

    customer.addInvoice(createInvoice(document));
    return customer;
  }

  private Invoice createInvoice(DocumentRequest document) {
    return Invoice.builder()
        .total(Money.of(document.getTotal(), Monetary.getCurrency(document.getCurrency())))
        .build();
  }

  private CalculateResponse createResponse(String outputCurrency, List<Customer> customers) {
    return CalculateResponse.builder()
        .currency(outputCurrency)
        .customers(
            customers.stream()
                .map(customer -> CustomerResponse.builder()
                    .name(customer.getName())
                    .balance(
                        Monetary.getDefaultRounding()
                            .apply(customer.calculateTotal(Monetary.getCurrency(outputCurrency), rateProvider))
                            .getNumber()
                            .doubleValue())
                    .build())
                .toArray(CustomerResponse[]::new)
        )
        .build();
  }
}
