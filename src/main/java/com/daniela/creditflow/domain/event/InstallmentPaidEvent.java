package com.daniela.creditflow.domain.event;

import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.domain.valueobject.InstallmentId;

import java.time.Instant;

public record InstallmentPaidEvent(CreditId creditId,
                                   InstallmentId installmentId,
                                   CustomerId customerId,
                                   Instant paidAt
) {
}
