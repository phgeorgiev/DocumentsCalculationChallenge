package com.designtechnologies.challenge.input;

import com.designtechnologies.challenge.core.commands.DocumentRequest;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvParser {

  public List<DocumentRequest> parse(File csv) throws IOException {
    try (MappingIterator<DocumentRequest> integrator = new CsvMapper()
        .readerFor(DocumentRequest.class)
        .with(buildCsvSchema())
        .readValues(csv)) {
      return integrator.readAll();
    }
  }

  private CsvSchema buildCsvSchema() {
    return CsvSchema.builder()
        .addColumn("customer")
        .addColumn("vatNumber")
        .addColumn("documentNumber")
        .addColumn("type")
        .addColumn("parentDocument")
        .addColumn("currency")
        .addColumn("total")
        .build()
        .withHeader();
  }
}
