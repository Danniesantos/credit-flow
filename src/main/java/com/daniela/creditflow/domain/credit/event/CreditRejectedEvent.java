package com.daniela.creditflow.domain.credit.event;

import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;

import java.time.Instant;

public record CreditRejectedEvent(CreditId creditId,
                                  CustomerId customerId,
                                  Instant occurredAt) {
}
