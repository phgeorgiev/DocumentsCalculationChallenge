package com.designtechnologies.challenge.core.commands.validators;

import com.designtechnologies.challenge.core.commands.CalculateRequest;
import com.designtechnologies.challenge.core.commands.DocumentRequest;

public class CalculateRequestValidator {

  public void validate(CalculateRequest request) throws InvalidCalculationRequestException {
    // matching example EUR:1.0,EUR:1
    if (!request.getExchangeRates().matches("^(([A-Z]{3}:([0-9]*[.])?[0-9]+)(,)?)+$")) {
      throw new InvalidCalculationRequestException("Invalid exchange rate");
    }

    for (DocumentRequest document : request.getDocuments()) {
      if (document.getTotal() < 0) {
        throw new InvalidCalculationRequestException("Document total can't be negative");
      }
    }
  }
}
