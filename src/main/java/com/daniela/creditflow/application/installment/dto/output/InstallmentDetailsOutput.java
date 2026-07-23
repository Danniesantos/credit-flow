package com.daniela.creditflow.application.installment.dto.output;

import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentDetailsOutput(UUID installmentId,
                                       Integer number,
                                       BigDecimal amount,
                                       LocalDate dueDate,
                                       PaymentMethod paymentMethod,
                                       InstallmentStatus status,
                                       Instant paidAt) {

}
