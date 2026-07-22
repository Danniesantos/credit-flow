package com.daniela.creditflow.infrastructure.web.response;

import com.daniela.creditflow.domain.installment.model.InstallmentStatus;
import com.daniela.creditflow.domain.installment.valueObject.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentDetailsResponse(UUID installmentId,
                                         Integer number,
                                         BigDecimal amount,
                                         LocalDate dueDate,
                                         PaymentMethod paymentMethod,
                                         InstallmentStatus status) {


}
