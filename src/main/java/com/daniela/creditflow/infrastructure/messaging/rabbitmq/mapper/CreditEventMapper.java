package com.daniela.creditflow.infrastructure.messaging.rabbitmq.mapper;

import com.daniela.creditflow.domain.credit.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.credit.event.CreditContractedEvent;
import com.daniela.creditflow.domain.credit.event.CreditRejectedEvent;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditEventType;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CreditEventMapper {

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

    public CreditMessage toMessage(CreditContractedEvent event) {
        return toMessage(
                event.creditId().value(),
                event.customerId().value(),
                event.occurredAt(),
                CreditEventType.CONTRACTED
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
}
