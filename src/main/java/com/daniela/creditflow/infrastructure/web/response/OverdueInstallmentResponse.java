package com.daniela.creditflow.infrastructure.web.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record OverdueInstallmentResponse(UUID id,
                                         Integer number,
                                         BigDecimal amount,
                                         LocalDate dueDate,
                                         long overdueDays) {
}
