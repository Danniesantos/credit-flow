package com.daniela.creditflow.application.credit.dto.output;

import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreditDetailsOutput(UUID creditId,
                                  UUID customerId,
                                  BigDecimal requestedAmount,
                                  BigDecimal interestRate,
                                  CreditType creditType,
                                  CreditStatus status,
                                  List<InstallmentDetailsOutput> installments,
                                  Instant createdAt,
                                  Instant updatedAt) {

}
