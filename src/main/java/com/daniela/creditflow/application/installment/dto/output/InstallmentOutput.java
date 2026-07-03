package com.daniela.creditflow.application.installment.dto.output;

import com.daniela.creditflow.domain.installment.model.InstallmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InstallmentOutput(UUID installmentId,
                                Integer number,
                                BigDecimal amount,
                                LocalDate dueDate,
                                InstallmentStatus status) {

}
