package com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher;

import com.daniela.creditflow.domain.event.*;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.mapper.RabbitEventMapper;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitCreditEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitEventMapper mapper;

    @InjectMocks
    private RabbitCreditEventPublisher publisher;

    @Test
    void shouldPublishApprovedEvent() {
        CreditApprovedEvent event = mock(CreditApprovedEvent.class);
        CreditMessage message = mock(CreditMessage.class);

        when(mapper.toMessage(event)).thenReturn(message);

        publisher.publishApproved(event);

        verify(mapper).toMessage(event);
        verify(rabbitTemplate).convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                RabbitConstants.CREDIT_APPROVED_ROUTING_KEY,
                message
        );
    }

    @Test
    void shouldPublishRejectedEvent() {
        CreditRejectedEvent event = mock(CreditRejectedEvent.class);
        CreditMessage message = mock(CreditMessage.class);

        when(mapper.toMessage(event)).thenReturn(message);

        publisher.publishRejected(event);

        verify(mapper).toMessage(event);
        verify(rabbitTemplate).convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                RabbitConstants.CREDIT_REJECTED_ROUTING_KEY,
                message
        );
    }

    @Test
    void shouldPublishCanceledEvent() {
        CreditCanceledEvent event = mock(CreditCanceledEvent.class);
        CreditMessage message = mock(CreditMessage.class);

        when(mapper.toMessage(event)).thenReturn(message);

        publisher.publishCanceled(event);

        verify(mapper).toMessage(event);
        verify(rabbitTemplate).convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                RabbitConstants.CREDIT_CANCELED_ROUTING_KEY,
                message
        );
    }

    @Test
    void shouldPublishContractedEvent() {
        CreditContractedEvent event = mock(CreditContractedEvent.class);
        CreditMessage message = mock(CreditMessage.class);

        when(mapper.toMessage(event)).thenReturn(message);

        publisher.publishContracted(event);

        verify(mapper).toMessage(event);
        verify(rabbitTemplate).convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY,
                message
        );
    }

    @Test
    void shouldPublishRenegotiatedEvent() {
        CreditRenegotiatedEvent event = mock(CreditRenegotiatedEvent.class);
        CreditMessage message = mock(CreditMessage.class);

        when(mapper.toMessage(event)).thenReturn(message);

        publisher.publishRenegotiated(event);

        verify(mapper).toMessage(event);
        verify(rabbitTemplate).convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                RabbitConstants.CREDIT_RENEGOTIATED_ROUTING_KEY,
                message
        );
    }

    @Test
    void shouldPublishRestructuredEvent() {
        CreditRestructuredEvent event = mock(CreditRestructuredEvent.class);
        CreditMessage message = mock(CreditMessage.class);

        when(mapper.toMessage(event)).thenReturn(message);

        publisher.publishRestructured(event);

        verify(mapper).toMessage(event);
        verify(rabbitTemplate).convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                RabbitConstants.CREDIT_RESTRUCTURED_ROUTING_KEY,
                message
        );
    }

    @Test
    void shouldPublishPaymentEvent() {
        InstallmentPaidEvent event = mock(InstallmentPaidEvent.class);
        PaidMessage message = mock(PaidMessage.class);

        when(mapper.toMessage(event)).thenReturn(message);

        publisher.publishPayment(event);

        verify(mapper).toMessage(event);
        verify(rabbitTemplate).convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                RabbitConstants.CREDIT_PAYMENT_ROUTING_KEY,
                message
        );
    }
}