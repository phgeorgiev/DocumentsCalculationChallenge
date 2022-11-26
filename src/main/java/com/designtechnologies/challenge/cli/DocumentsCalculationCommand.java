package com.designtechnologies.challenge.cli;

import com.designtechnologies.challenge.core.commands.CalculateHandler;
import com.designtechnologies.challenge.core.commands.CalculateRequest;
import com.designtechnologies.challenge.core.commands.DocumentRequest;
import com.designtechnologies.challenge.core.commands.validators.CalculateRequestValidator;
import com.designtechnologies.challenge.core.commands.validators.InvalidCalculationRequestException;
import com.designtechnologies.challenge.input.CsvParser;
import java.io.IOException;
import java.nio.file.Paths;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "documents-calculate",
    description = "Sum invoice documents in different currencies via a CSV file.",
    mixinStandardHelpOptions = true
)
public class DocumentsCalculationCommand implements Runnable {

  @Parameters(index = "0", arity = "1", description = "Path of the CSV file containing the documents")
  private String filePath;

  @Parameters(index = "1", arity = "1", description = "A list of currencies and exchange rates. The currencies can have different exchange rates, based on a default currency: EUR:GBP, EUR:BGN and so on. The default currency is specified by giving it an exchange rate of 1.\n(Example: EUR:1,USD:0.987,GBP:0.878)")
  private String exhangeRates;

  @Parameters(index = "2", arity = "1", description = "Output currency")
  private String outputCurrency;

  @Option(names = {"-v", "--vat"}, description = "Summed documents for specified customer by vat number")
  private String vatNumber;

  private final CalculateHandler handler;
  private final OutputFormatter outputFormatter;
  private final CalculateRequestValidator validator;

  public DocumentsCalculationCommand(CalculateHandler handler, OutputFormatter outputFormatter, CalculateRequestValidator validator) {
    this.handler = handler;
    this.outputFormatter = outputFormatter;
    this.validator = validator;
  }

  @Override
  public void run() {
    try {
      CalculateRequest request = CalculateRequest.builder()
          .exchangeRates(exhangeRates)
          .outputCurrency(outputCurrency)
          .vatNumber(vatNumber)
          .documents(
              new CsvParser().parse(Paths.get(filePath).toFile()).toArray(new DocumentRequest[0]))
          .build();

      validator.validate(request);

      outputFormatter
          .format(handler.handle(request))
          .forEach(System.out::println);
    } catch (IOException | RuntimeException | InvalidCalculationRequestException e) {
      System.out.println(e.getMessage());
    }
  }
}
