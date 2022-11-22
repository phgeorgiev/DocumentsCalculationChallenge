package core.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.designtechnologies.challenge.core.commands.CalculateHandler;
import com.designtechnologies.challenge.core.commands.CalculateRequest;
import com.designtechnologies.challenge.core.commands.CalculateResponse;
import com.designtechnologies.challenge.core.commands.CustomerResponse;
import com.designtechnologies.challenge.core.commands.DocumentRequest;
import com.designtechnologies.challenge.core.documents.CustomerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculateHandlerTest {

  CalculateHandler useCase;

  @BeforeEach
  public void setUp() {
    useCase = new CalculateHandler(new CustomerRegistry());
  }

  @Test
  public void calculateInvoiceTotal() {
    CalculateRequest request = CalculateRequest.builder().documents(new DocumentRequest[]{
        DocumentRequest.builder()
            .customer("Vendor 1")
            .vatNumber("123456789")
            .documentNumber(1000000257)
            .type(1)
            .currency("USD")
            .total(400)
            .build()
        })
        .build();

    CalculateResponse expectedResponse = CalculateResponse.builder()
        .customers(
            new CustomerResponse[]{
                CustomerResponse.builder()
                    .balance(400)
                    .name("Vendor 1")
                    .build()
            }
        )
        .build();

    assertThat(useCase.handle(request), equalTo(expectedResponse));
  }
}
