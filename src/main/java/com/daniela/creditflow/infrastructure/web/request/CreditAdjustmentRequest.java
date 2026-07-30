package com.daniela.creditflow.infrastructure.web.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CreditAdjustmentRequest {

    @Min(2)
    private int installmentsQuantity;
}
