package com.designtechnologies.challenge.core.documents;

import javax.money.MonetaryAmount;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class AbstractDocument {

  protected int number;
  protected MonetaryAmount amount;
}
