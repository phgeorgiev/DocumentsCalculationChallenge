package com.designtechnologies.challenge.core.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalculateResponse {

  private String currency;
  private CustomerResponse[] customers;
}
