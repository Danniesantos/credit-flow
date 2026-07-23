package com.daniela.creditflow.infrastructure.web.response;

import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreditDetailsResponse(UUID creditId,
                                    UUID customerId,
                                    BigDecimal requestedAmount,
                                    BigDecimal interestRate,
                                    CreditType creditType,
                                    CreditStatus status,
                                    List<InstallmentDetailsResponse> installments,
                                    Instant createdAt,
                                    Instant updatedAt) {

}
