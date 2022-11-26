package com.designtechnologies.challenge.core.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.designtechnologies.challenge.core.documents.CustomExchangeRateProvider;
import com.designtechnologies.challenge.core.documents.CustomerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculateHandlerTest {

  CalculateHandler useCase;

  @BeforeEach
  public void setUp() {
    useCase = new CalculateHandler(new CustomerRegistry(), new CustomExchangeRateProvider());
  }

  @Test
  public void calculateInvoiceTotal() {
    CalculateRequest request = CalculateRequest.builder()
        .exchangeRates("EUR:1,USD:0.987,GBP:0.878")
        .outputCurrency("EUR")
        .documents(new DocumentRequest[]{
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000257)
                .type(1)
                .currency("USD")
                .total(400)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 2")
                .vatNumber("987654321")
                .documentNumber(1000000258)
                .type(1)
                .currency("EUR")
                .total(900)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 3")
                .vatNumber("123465123")
                .documentNumber(1000000259)
                .type(1)
                .currency("GBP")
                .total(1300)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000260)
                .parentDocument(1000000257)
                .type(2)
                .currency("EUR")
                .total(100)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000261)
                .parentDocument(1000000257)
                .type(3)
                .currency("GBP")
                .total(50)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000264)
                .type(1)
                .currency("EUR")
                .total(1600)
                .build()
        })
        .build();

    CalculateResponse expectedResponse = CalculateResponse.builder()
        .currency("EUR")
        .customers(
            new CustomerResponse[]{
                CustomerResponse.builder()
                    .balance(1962.22)
                    .name("Vendor 1")
                    .build(),
                CustomerResponse.builder()
                    .balance(900)
                    .name("Vendor 2")
                    .build(),
                CustomerResponse.builder()
                    .balance(1480.64)
                    .name("Vendor 3")
                    .build()
            }
        )
        .build();

    assertThat(useCase.handle(request), equalTo(expectedResponse));
  }

  @Test
  public void throwErrorDefaultCurrencyNotSpecified() {
    CalculateRequest request = CalculateRequest.builder()
        .exchangeRates("EUR:0.5,USD:0.987,GBP:0.878")
        .outputCurrency("EUR")
        .documents(new DocumentRequest[]{
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

    RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.handle(request));
    assertThat(exception.getMessage(), equalTo("Default currency must be specified by giving it an exchange rate of 1"));
  }

  @Test
  public void throwErrorWhenTheTotalOfAllCreditNotesIsBiggerThanSumOfTheInvoice() {
    CalculateRequest request = CalculateRequest.builder()
        .exchangeRates("EUR:1,USD:0.987,GBP:0.878")
        .outputCurrency("EUR")
        .documents(new DocumentRequest[]{
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000257)
                .type(1)
                .currency("USD")
                .total(400)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000260)
                .parentDocument(1000000257)
                .type(2)
                .currency("EUR")
                .total(100)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000261)
                .parentDocument(1000000257)
                .type(2)
                .currency("GBP")
                .total(350)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000264)
                .type(1)
                .currency("EUR")
                .total(1600)
                .build()
        })
        .build();

    RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.handle(request));
    assertThat(exception.getMessage(), equalTo("Invoice (1000000257) sum must greater or equal to 0"));
  }

  @Test
  public void throwErrorOnMissingSpecifiedParent() {
    CalculateRequest request = CalculateRequest.builder()
        .exchangeRates("EUR:1,USD:0.987,GBP:0.878")
        .outputCurrency("EUR")
        .documents(new DocumentRequest[]{
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000257)
                .type(1)
                .currency("USD")
                .total(400)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000260)
                .parentDocument(1000000000)
                .type(2)
                .currency("EUR")
                .total(100)
                .build()
        })
        .build();

    RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.handle(request));
    assertThat(exception.getMessage(), equalTo("Parent document (1000000000) is missing"));
  }

  @Test
  public void throwErrorOnNoteWithoutParent() {
    CalculateRequest request = CalculateRequest.builder()
        .exchangeRates("EUR:1,USD:0.987,GBP:0.878")
        .outputCurrency("EUR")
        .documents(new DocumentRequest[]{
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000257)
                .type(1)
                .currency("USD")
                .total(400)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000260)
                .type(2)
                .currency("EUR")
                .total(100)
                .build()
        })
        .build();

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> useCase.handle(request));
    assertThat(exception.getMessage(), equalTo("Parent document is required"));
  }

  @Test
  public void calculateInvoiceTotalForGivenVatNumber() {
    CalculateRequest request = CalculateRequest.builder()
        .exchangeRates("EUR:1,USD:0.987,GBP:0.878")
        .outputCurrency("EUR")
        .vatNumber("123456789")
        .documents(new DocumentRequest[]{
            DocumentRequest.builder()
                .customer("Vendor 1")
                .vatNumber("123456789")
                .documentNumber(1000000257)
                .type(1)
                .currency("USD")
                .total(400)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 2")
                .vatNumber("987654321")
                .documentNumber(1000000258)
                .type(1)
                .currency("EUR")
                .total(900)
                .build(),
            DocumentRequest.builder()
                .customer("Vendor 3")
                .vatNumber("123465123")
                .documentNumber(1000000259)
                .type(1)
                .currency("GBP")
                .total(1300)
                .build()
        })
        .build();

    CalculateResponse expectedResponse = CalculateResponse.builder()
        .currency("EUR")
        .customers(
            new CustomerResponse[]{
                CustomerResponse.builder()
                    .balance(405.27)
                    .name("Vendor 1")
                    .build()
            }
        )
        .build();

    assertThat(useCase.handle(request), equalTo(expectedResponse));
  }

  @Test
  public void throwErrorOnMissingCustomer() {
    CalculateRequest request = CalculateRequest.builder()
        .exchangeRates("EUR:1,USD:0.987,GBP:0.878")
        .outputCurrency("EUR")
        .vatNumber("missingNumber")
        .documents(new DocumentRequest[]{
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

    RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.handle(request));
    assertThat(exception.getMessage(), equalTo("Missing customer (missingNumber)"));
  }
}
