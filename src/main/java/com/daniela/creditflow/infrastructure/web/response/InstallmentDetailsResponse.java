package com.daniela.creditflow.infrastructure.web.response;

import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentDetailsResponse(UUID installmentId,
                                         Integer number,
                                         BigDecimal amount,
                                         LocalDate dueDate,
                                         PaymentMethod paymentMethod,
                                         InstallmentStatus status,
                                         Instant paidAt) {


}
