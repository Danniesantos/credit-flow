package com.daniela.creditflow.infrastructure.web.request;

import com.daniela.creditflow.domain.model.PaymentMethod;

import java.util.UUID;

public record PaymentRequest(UUID creditId,
                             PaymentMethod paymentMethod) {


}
