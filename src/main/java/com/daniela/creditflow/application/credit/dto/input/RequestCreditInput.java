package com.daniela.creditflow.application.credit.dto.input;

import com.daniela.creditflow.domain.credit.model.CreditType;

import java.math.BigDecimal;
import java.util.UUID;

public record RequestCreditInput(UUID customerId,
                                 BigDecimal requestedAmount,
                                 Integer installments,
                                 CreditType creditType) {
}
