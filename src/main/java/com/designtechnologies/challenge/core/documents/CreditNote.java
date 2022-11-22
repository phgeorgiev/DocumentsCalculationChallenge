package com.designtechnologies.challenge.core.documents;

import javax.money.MonetaryAmount;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CreditNote extends AbstractDocument {

  @Override
  public MonetaryAmount getAmount() {
    return amount.negate();
  }
}
