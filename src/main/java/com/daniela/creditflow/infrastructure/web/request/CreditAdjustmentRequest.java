package com.daniela.creditflow.infrastructure.web.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditAdjustmentRequest {

    @NotNull(message = "Installments quantity is required")
    @Min(value = 2, message = "Minimum installments quantity is 2")
    private Integer installmentsQuantity;
}
