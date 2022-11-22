package com.designtechnologies.challenge.core.documents;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerRegistry {

  private final Map<String, Customer> customers;

  public CustomerRegistry() {
    customers = new HashMap<>();
  }

  public void addCustomer(Customer customer) {
    customers.put(customer.getName(), customer);
  }

  public Optional<Customer> getCustomer(String name) {
    return Optional.ofNullable(customers.getOrDefault(name, null));
  }
}

