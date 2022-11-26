package com.designtechnologies.challenge.cli;

import com.designtechnologies.challenge.core.commands.CalculateResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import picocli.CommandLine.Help.Ansi;

public class OutputFormatter {

  public List<String> format(CalculateResponse response) {
    return Arrays.stream(response.getCustomers())
        .map(customer -> Ansi.AUTO.string(
            String.format(
                "Customer @|bold,fg(yellow) %s|@ - %.2f %s",
                customer.getName(),
                customer.getBalance(),
                response.getCurrency()
            )
        ))
        .collect(Collectors.toList());
  }
}
