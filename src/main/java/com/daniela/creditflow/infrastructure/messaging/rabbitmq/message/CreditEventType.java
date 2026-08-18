package com.daniela.creditflow.infrastructure.messaging.rabbitmq.message;

public enum CreditEventType {
    APPROVED,
    REJECTED,
    CANCELED,
    CONTRACTED,
    RENEGOTIATED,
    RESTRUCTURED,
    PAID
}
