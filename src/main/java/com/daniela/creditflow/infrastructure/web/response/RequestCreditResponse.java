package com.daniela.creditflow.infrastructure.web.response;

import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.domain.credit.model.CreditType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RequestCreditResponse(UUID creditId,
                                    UUID customerId,
                                    BigDecimal requestedAmount,
                                    Integer installments,
                                    BigDecimal interestRate,
                                    CreditType creditType,
                                    CreditStatus status,
                                    Instant createdAt,
                                    Instant updatedAt) {

}
