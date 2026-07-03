package com.daniela.creditflow.application.installment.dto.input;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentInput(Integer number,
                               BigDecimal amount,
                               LocalDate dueDate) {

}
