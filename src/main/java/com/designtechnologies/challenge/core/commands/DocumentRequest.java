package com.designtechnologies.challenge.core.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentRequest {

  private String customer;
  private String vatNumber;
  private Integer documentNumber;
  private int type;
  private Integer parentDocument;
  private String currency;
  private double total;
}
