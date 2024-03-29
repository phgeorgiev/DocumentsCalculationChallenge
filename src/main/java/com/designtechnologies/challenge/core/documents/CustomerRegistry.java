package com.designtechnologies.challenge.core.documents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRegistry {

  private final Map<String, Customer> customers;

  public CustomerRegistry() {
    customers = new HashMap<>();
  }

  public void addCustomer(Customer customer) {
    customers.put(customer.getVatNumber(), customer);
  }

  public Optional<Customer> getCustomer(String vatNumber) {
    return Optional.ofNullable(customers.getOrDefault(vatNumber, null));
  }

  public List<Customer> getAll() {
    return new ArrayList<>(customers.values());
  }
}

