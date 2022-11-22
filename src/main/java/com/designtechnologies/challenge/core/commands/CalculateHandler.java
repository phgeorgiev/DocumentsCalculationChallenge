package com.designtechnologies.challenge.core.commands;

import com.designtechnologies.challenge.core.documents.AbstractDocument;
import com.designtechnologies.challenge.core.documents.CreditNote;
import com.designtechnologies.challenge.core.documents.CustomExchangeRateProvider;
import com.designtechnologies.challenge.core.documents.Customer;
import com.designtechnologies.challenge.core.documents.CustomerRegistry;
import com.designtechnologies.challenge.core.documents.DebitNote;
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
        .map(this::createCustomerWithInvoices)
        .forEach(customerRegistry::addCustomer);

    Arrays.stream(request.getDocuments())
        .forEach(this::addNoteToInvoice);

    return createResponse(request.getOutputCurrency(), customerRegistry.getAll());
  }

  private Customer createCustomerWithInvoices(DocumentRequest document) {
    Customer customer = customerRegistry.getCustomer(document.getCustomer())
        .orElseGet(() -> Customer.builder()
            .name(document.getCustomer())
            .vatNumber(document.getVatNumber())
            .build());

    if (document.getType() == 1) {
      customer.addInvoice(createInvoice(document));
    }

    return customer;
  }

  private Invoice createInvoice(DocumentRequest document) {
    return Invoice.builder()
        .number(document.getDocumentNumber())
        .amount(Money.of(document.getTotal(), Monetary.getCurrency(document.getCurrency())))
        .build();
  }

  private void addNoteToInvoice(DocumentRequest document) {
    if (document.getType() == 1) {
      return;
    }

    if (document.getParentDocument() == null) {
      throw new IllegalArgumentException("Parent document is required");
    }

    customerRegistry
        .getCustomer(document.getCustomer())
        .ifPresent(customer -> customer
            .getInvoice(document.getParentDocument())
            .orElseThrow(() -> new RuntimeException("Parent document (%d) is missing".formatted(document.getParentDocument())))
            .addNote(createNote(document))
        );
  }

  private AbstractDocument createNote(DocumentRequest document) {
    if (document.getType() != 2 && document.getType() != 3) {
      throw new RuntimeException("Invalid type");
    }

    var note = document.getType() == 2
        ? CreditNote.builder()
        : DebitNote.builder();

    return note
        .number(document.getDocumentNumber())
        .amount(Money.of(document.getTotal(), Monetary.getCurrency(document.getCurrency())))
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
