package com.daniela.creditflow.infrastructure.messaging.rabbitmq.mapper;

import com.daniela.creditflow.domain.event.*;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditEventType;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RabbitEventMapperTest {

    private final RabbitEventMapper mapper = new RabbitEventMapper();

    @Test
    void shouldMapCreditApprovedEvent() {
        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        CreditApprovedEvent event = new CreditApprovedEvent(
                new CreditId(creditId),
                new CustomerId(customerId),
                occurredAt
        );

        CreditMessage message = mapper.toMessage(event);

        assertEquals(creditId, message.creditId());
        assertEquals(customerId, message.customerId());
        assertEquals(occurredAt, message.occurredAt());
        assertEquals(CreditEventType.APPROVED, message.eventType());
    }

    @Test
    void shouldMapCreditRejectedEvent() {
        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        CreditRejectedEvent event = new CreditRejectedEvent(
                new CreditId(creditId),
                new CustomerId(customerId),
                occurredAt
        );

        CreditMessage message = mapper.toMessage(event);

        assertEquals(creditId, message.creditId());
        assertEquals(customerId, message.customerId());
        assertEquals(occurredAt, message.occurredAt());
        assertEquals(CreditEventType.REJECTED, message.eventType());
    }

    @Test
    void shouldMapCreditCanceledEvent() {
        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        CreditCanceledEvent event = new CreditCanceledEvent(
                new CreditId(creditId),
                new CustomerId(customerId),
                occurredAt
        );

        CreditMessage message = mapper.toMessage(event);

        assertEquals(creditId, message.creditId());
        assertEquals(customerId, message.customerId());
        assertEquals(occurredAt, message.occurredAt());
        assertEquals(CreditEventType.CANCELED, message.eventType());
    }

    @Test
    void shouldMapCreditContractedEvent() {
        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        CreditContractedEvent event = new CreditContractedEvent(
                new CreditId(creditId),
                new CustomerId(customerId),
                occurredAt
        );

        CreditMessage message = mapper.toMessage(event);

        assertEquals(creditId, message.creditId());
        assertEquals(customerId, message.customerId());
        assertEquals(occurredAt, message.occurredAt());
        assertEquals(CreditEventType.CONTRACTED, message.eventType());
    }

    @Test
    void shouldMapCreditRenegotiatedEvent() {
        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        CreditRenegotiatedEvent event = new CreditRenegotiatedEvent(
                new CreditId(creditId),
                new CustomerId(customerId),
                occurredAt
        );

        CreditMessage message = mapper.toMessage(event);

        assertEquals(creditId, message.creditId());
        assertEquals(customerId, message.customerId());
        assertEquals(occurredAt, message.occurredAt());
        assertEquals(CreditEventType.RENEGOTIATED, message.eventType());
    }

    @Test
    void shouldMapCreditRestructuredEvent() {
        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Instant occurredAt = Instant.now();

        CreditRestructuredEvent event = new CreditRestructuredEvent(
                new CreditId(creditId),
                new CustomerId(customerId),
                occurredAt
        );

        CreditMessage message = mapper.toMessage(event);

        assertEquals(creditId, message.creditId());
        assertEquals(customerId, message.customerId());
        assertEquals(occurredAt, message.occurredAt());
        assertEquals(CreditEventType.RESTRUCTURED, message.eventType());
    }
}
