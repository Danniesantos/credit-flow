package com.daniela.creditflow.application.credit.dto.output;

import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.domain.credit.model.CreditType;
import com.daniela.creditflow.domain.credit.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RequestCreditOutput(UUID creditId,
                                  UUID customerId,
                                  BigDecimal requestedAmount,
                                  Integer installments,
                                  BigDecimal interestRate,
                                  CreditType creditType,
                                  PaymentMethod paymentMethod,
                                  CreditStatus status,
                                  Instant createdAt,
                                  Instant updatedAt) {
}
