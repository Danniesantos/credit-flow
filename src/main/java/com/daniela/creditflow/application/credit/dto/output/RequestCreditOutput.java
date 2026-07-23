package com.daniela.creditflow.application.credit.dto.output;

import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RequestCreditOutput(UUID creditId,
                                  UUID customerId,
                                  BigDecimal requestedAmount,
                                  Integer installments,
                                  BigDecimal interestRate,
                                  CreditType creditType,
                                  CreditStatus status,
                                  Instant createdAt,
                                  Instant updatedAt) {
}
