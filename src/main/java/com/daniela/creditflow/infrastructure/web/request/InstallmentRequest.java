package com.daniela.creditflow.infrastructure.web.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentRequest(Integer number,
                                 BigDecimal amount,
                                 LocalDate dueDate) {


}
