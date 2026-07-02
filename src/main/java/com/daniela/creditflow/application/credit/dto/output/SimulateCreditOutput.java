package com.daniela.creditflow.application.credit.dto.output;

import java.math.BigDecimal;

public record SimulateCreditOutput(BigDecimal requestedAmount,
                                   BigDecimal interestRate,
                                   BigDecimal totalAmount,
                                   Integer installments,
                                   BigDecimal installmentAmount) {

}
