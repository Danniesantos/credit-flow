package com.daniela.creditflow.domain.event;

import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import com.daniela.creditflow.domain.valueObject.InstallmentId;

import java.time.Instant;

public record InstallmentPaidEvent(CreditId creditId,
                                   InstallmentId installmentId,
                                   CustomerId customerId,
                                   Instant paidAt
) {
}
