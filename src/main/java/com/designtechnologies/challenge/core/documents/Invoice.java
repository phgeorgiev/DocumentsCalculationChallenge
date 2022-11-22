package com.designtechnologies.challenge.core.documents;

import javax.money.MonetaryAmount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Invoice {

  private MonetaryAmount total;
}
