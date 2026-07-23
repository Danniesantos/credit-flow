package com.daniela.creditflow.application.installment.dto.input;

import com.daniela.creditflow.domain.model.PaymentMethod;

import java.util.UUID;

public record PaymentInstallmentInput(UUID creditId,
                                      UUID installmentId,
                                      PaymentMethod paymentMethod) {
}
