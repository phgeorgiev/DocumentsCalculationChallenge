package com.designtechnologies.challenge;

import com.designtechnologies.challenge.cli.DocumentsCalculationCommandFactory;
import picocli.CommandLine;

public class Application {

  public static void main(String[] args) {
    int exitCode = new CommandLine(new DocumentsCalculationCommandFactory().create()).execute(args);
    System.exit(exitCode);
  }
}