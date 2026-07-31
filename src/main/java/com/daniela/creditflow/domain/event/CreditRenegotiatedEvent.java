package com.daniela.creditflow.domain.event;

import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.valueObject.CustomerId;

import java.time.Instant;

public record CreditRenegotiatedEvent(CreditId creditId,
                                      CustomerId customerId,
                                      Instant occurredAt) {
}
