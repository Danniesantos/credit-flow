package com.daniela.creditflow.infrastructure.web.request;

import com.daniela.creditflow.domain.credit.model.CreditType;
import com.daniela.creditflow.domain.credit.model.PaymentMethod;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class RequestCreditRequest {

    @NotNull(message = "Customer id is required")
    private UUID customerId;

    @NotNull(message = "Requested amount  is required")
    @Positive(message = "Requested amount must be greater than zero")
    private BigDecimal requestedAmount;

    @NotNull(message = "Number of installments is required")
    @Min(value = 1, message = "Minimum installments is 1")
    @Max(value = 60, message = "Maximum installments is 60")
    private Integer installments;

    @NotNull(message = "credit type is required")
    private CreditType creditType;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
