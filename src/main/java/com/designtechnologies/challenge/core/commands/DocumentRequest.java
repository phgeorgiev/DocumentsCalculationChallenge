package com.designtechnologies.challenge.core.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentRequest {

  private String customer;
  private String vatNumber;
  private int documentNumber;
  private int type;
  private int parentDocument;
  private String currency;
  private double total;
}
