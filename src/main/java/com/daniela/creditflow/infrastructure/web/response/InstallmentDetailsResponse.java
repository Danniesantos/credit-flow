package com.daniela.creditflow.infrastructure.web.response;

import com.daniela.creditflow.domain.installment.model.InstallmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentDetailsResponse(UUID installmentId,
                                         Integer number,
                                         BigDecimal amount,
                                         LocalDate dueDate,
                                         InstallmentStatus status) {


}
