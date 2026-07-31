package com.daniela.creditflow.infrastructure.messaging.rabbitmq.mapper;

import com.daniela.creditflow.domain.event.*;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditEventType;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class RabbitEventMapper {

    public CreditMessage toMessage(CreditApprovedEvent event) {
        return toMessage(
                event.creditId().value(),
                event.customerId().value(),
                event.occurredAt(),
                CreditEventType.APPROVED
        );
    }

    public CreditMessage toMessage(CreditRejectedEvent event) {
        return toMessage(
                event.creditId().value(),
                event.customerId().value(),
                event.occurredAt(),
                CreditEventType.REJECTED
        );
    }

    public CreditMessage toMessage(CreditCanceledEvent event) {
        return toMessage(
                event.creditId().value(),
                event.customerId().value(),
                event.occurredAt(),
                CreditEventType.CANCELED
        );
    }

    public CreditMessage toMessage(CreditContractedEvent event) {
        return toMessage(
                event.creditId().value(),
                event.customerId().value(),
                event.occurredAt(),
                CreditEventType.CONTRACTED
        );
    }

    public CreditMessage toMessage(CreditRenegotiatedEvent event) {
        return toMessage(
                event.creditId().value(),
                event.customerId().value(),
                event.occurredAt(),
                CreditEventType.RENEGOTIATED
        );
    }

    public CreditMessage toMessage(CreditRestructuredEvent event) {
        return toMessage(
                event.creditId().value(),
                event.customerId().value(),
                event.occurredAt(),
                CreditEventType.RESTRUCTURED
        );
    }

    public PaidMessage toMessage(InstallmentPaidEvent event) {
        return toMessage(
                event.creditId().value(),
                event.installmentId().value(),
                event.customerId().value(),
                event.paidAt()
        );
    }

    private CreditMessage toMessage(
            UUID creditId,
            UUID customerId,
            Instant occurredAt,
            CreditEventType eventType
    ) {
        return new CreditMessage(
                creditId,
                customerId,
                occurredAt,
                eventType
        );
    }

    private PaidMessage toMessage(
            UUID creditId,
            UUID installmentId,
            UUID customerId,
            Instant paidAt
    ) {
        return new PaidMessage(
                creditId,
                installmentId,
                customerId,
                paidAt,
                CreditEventType.PAID
        );
    }
}
