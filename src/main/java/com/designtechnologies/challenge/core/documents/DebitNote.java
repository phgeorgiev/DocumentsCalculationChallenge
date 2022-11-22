package com.designtechnologies.challenge.core.documents;

import javax.money.MonetaryAmount;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class DebitNote extends AbstractDocument {

  public MonetaryAmount getAmount() {
    return amount;
  }
}
