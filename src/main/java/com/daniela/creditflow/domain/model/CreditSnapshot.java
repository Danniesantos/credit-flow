package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.domain.valueobject.InterestRate;
import com.daniela.creditflow.domain.valueobject.Money;

import java.time.Instant;
import java.util.List;

public record CreditSnapshot(CreditId id,
                             CustomerId customerId,
                             Money requestedAmount,
                             CreditType creditType,
                             InterestRate interestRate,
                             Integer installmentsQuantity,
                             CreditStatus status,
                             Instant createdAt,
                             Instant updatedAt,
                             List<Installment> installments
) {
}
