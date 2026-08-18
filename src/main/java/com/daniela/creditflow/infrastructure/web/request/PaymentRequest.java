package com.daniela.creditflow.infrastructure.web.request;

import com.daniela.creditflow.domain.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PaymentRequest(@NotNull(message = "Credit id is required")
                             UUID creditId,

                             @NotNull(message = "Payment method is required")
                             PaymentMethod paymentMethod) {
}
