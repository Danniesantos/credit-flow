package com.daniela.creditflow.infrastructure.web.response;

import java.math.BigDecimal;

public record SimulateCreditResponse(BigDecimal requestedAmount,
                                     BigDecimal interestRate,
                                     BigDecimal totalAmount,
                                     Integer installments,
                                     BigDecimal installmentAmount) {

}
