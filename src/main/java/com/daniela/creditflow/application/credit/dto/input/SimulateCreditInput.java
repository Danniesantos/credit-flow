package com.daniela.creditflow.application.credit.dto.input;

import com.daniela.creditflow.domain.model.CreditType;

import java.math.BigDecimal;

public record SimulateCreditInput(BigDecimal requestedAmount,
                                  Integer installments,
                                  CreditType creditType) {
}
