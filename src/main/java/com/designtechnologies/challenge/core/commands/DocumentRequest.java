package com.designtechnologies.challenge.core.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

  private String customer;
  private String vatNumber;
  private Integer documentNumber;
  private int type;
  private Integer parentDocument;
  private String currency;
  private double total;
}
