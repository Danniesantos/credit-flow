package com.daniela.creditflow.infrastructure.messaging.rabbitmq.message;

import java.time.Instant;
import java.util.UUID;

public record PaidMessage(UUID creditId,
                          UUID installmentId,
                          UUID customerId,
                          Instant paidAt,
                          CreditEventType creditEventType) {
}
