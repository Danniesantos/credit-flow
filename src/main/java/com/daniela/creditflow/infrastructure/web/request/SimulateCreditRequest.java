package com.daniela.creditflow.infrastructure.web.request;

import com.daniela.creditflow.domain.credit.model.CreditType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SimulateCreditRequest {

    @NotNull(message = "Requested amount is required")
    @Positive(message = "Requested amount must be greater than zero")
    private BigDecimal requestedAmount;

    @NotNull(message = "Number of installments is required")
    @Min(value = 1, message = "Minimum installments is 1")
    @Max(value = 60, message = "Maximum installments is 60")
    private Integer installments;

    @NotNull(message = "Credit type is required")
    private CreditType creditType;
}
