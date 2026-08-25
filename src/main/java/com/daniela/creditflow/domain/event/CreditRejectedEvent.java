package com.daniela.creditflow.domain.event;

import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.CustomerId;

import java.time.Instant;

public record CreditRejectedEvent(CreditId creditId,
                                  CustomerId customerId,
                                  Instant occurredAt) {
}
