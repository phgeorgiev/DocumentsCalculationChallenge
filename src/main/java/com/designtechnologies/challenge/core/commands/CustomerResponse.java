package com.designtechnologies.challenge.core.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {

  private double balance;
  private String name;
}
