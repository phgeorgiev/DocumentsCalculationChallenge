package com.designtechnologies.challenge.cli;

import com.designtechnologies.challenge.core.commands.CalculateHandler;
import com.designtechnologies.challenge.core.commands.validators.CalculateRequestValidator;
import com.designtechnologies.challenge.core.documents.CustomExchangeRateProvider;
import com.designtechnologies.challenge.core.documents.CustomerRegistry;

public class DocumentsCalculationCommandFactory {

  public DocumentsCalculationCommand create() {
    return new DocumentsCalculationCommand(
        new CalculateHandler(new CustomerRegistry(), new CustomExchangeRateProvider()),
        new OutputFormatter(),
        new CalculateRequestValidator()
    );
  }
}
