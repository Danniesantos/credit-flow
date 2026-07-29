package com.daniela.creditflow.application.installment.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record OverdueInstallmentOutput(UUID id,
                                       Integer number,
                                       BigDecimal amount,
                                       LocalDate dueDate,
                                       long overdueDays) {

}
