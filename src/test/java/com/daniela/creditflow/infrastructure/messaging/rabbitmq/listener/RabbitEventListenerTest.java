package com.daniela.creditflow.infrastructure.messaging.rabbitmq.listener;

import com.daniela.creditflow.domain.event.*;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher.RabbitEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitEventListenerTest {

    @Mock
    private RabbitEventPublisher publisher;

    @InjectMocks
    private RabbitEventListener listener;

    @Test
    void shouldPublishApprovedEvent() {
        CreditApprovedEvent event = mock(CreditApprovedEvent.class);

        listener.handle(event);

        verify(publisher).publishApproved(event);
    }

    @Test
    void shouldPublishRejectedEvent() {
        CreditRejectedEvent event = mock(CreditRejectedEvent.class);

        listener.handle(event);

        verify(publisher).publishRejected(event);
    }

    @Test
    void shouldPublishCanceledEvent() {
        CreditCanceledEvent event = mock(CreditCanceledEvent.class);

        listener.handle(event);

        verify(publisher).publishCanceled(event);
    }

    @Test
    void shouldPublishContractedEvent() {
        CreditContractedEvent event = mock(CreditContractedEvent.class);

        listener.handle(event);

        verify(publisher).publishContracted(event);
    }

    @Test
    void shouldPublishRenegotiatedEvent() {
        CreditRenegotiatedEvent event = mock(CreditRenegotiatedEvent.class);

        listener.handle(event);

        verify(publisher).publishRenegotiated(event);
    }

    @Test
    void shouldPublishRestructuredEvent() {
        CreditRestructuredEvent event = mock(CreditRestructuredEvent.class);

        listener.handle(event);

        verify(publisher).publishRestructured(event);
    }

    @Test
    void shouldPublishPaymentEvent() {
        InstallmentPaidEvent event = mock(InstallmentPaidEvent.class);

        listener.handle(event);

        verify(publisher).publishPayment(event);
    }
}