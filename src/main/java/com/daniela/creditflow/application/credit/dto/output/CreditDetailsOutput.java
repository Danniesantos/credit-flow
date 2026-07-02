package com.daniela.creditflow.application.credit.dto.output;

import com.daniela.creditflow.application.installment.dto.output.InstallmentOutput;
import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.domain.credit.model.CreditType;
import com.daniela.creditflow.domain.credit.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreditDetailsOutput(UUID creditId,
                                  UUID customerId,
                                  BigDecimal requestedAmount,
                                  BigDecimal interestRate,
                                  CreditType creditType,
                                  PaymentMethod paymentMethod,
                                  CreditStatus status,
                                  List<InstallmentOutput> installments,
                                  Instant createdAt,
                                  Instant updatedAt) {

}
