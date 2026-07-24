package com.daniela.creditflow.infrastructure.messaging.rabbitmq.message;

import java.time.Instant;
import java.util.UUID;

public record CreditMessage(UUID creditId,
                            UUID customerId,
                            Instant occurredAt,
                            CreditEventType eventType) {
}
