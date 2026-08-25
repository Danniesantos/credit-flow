package com.daniela.creditflow.application.notification;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private final NotificationService service =
            new NotificationService();

    @Test
    void shouldNotifyApproved() {

        CreditMessage message = mock(CreditMessage.class);

        when(message.creditId()).thenReturn(UUID.randomUUID());
        when(message.customerId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() ->
                service.notifyApproved(message)
        );
    }

    @Test
    void shouldNotifyRejected() {

        CreditMessage message = mock(CreditMessage.class);

        when(message.creditId()).thenReturn(UUID.randomUUID());
        when(message.customerId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() ->
                service.notifyRejected(message)
        );
    }

    @Test
    void shouldNotifyCanceled() {

        CreditMessage message = mock(CreditMessage.class);

        when(message.creditId()).thenReturn(UUID.randomUUID());
        when(message.customerId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() ->
                service.notifyCanceled(message)
        );
    }

    @Test
    void shouldNotifyContracted() {

        CreditMessage message = mock(CreditMessage.class);

        when(message.creditId()).thenReturn(UUID.randomUUID());
        when(message.customerId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() ->
                service.notifyContracted(message)
        );
    }

    @Test
    void shouldNotifyRenegotiated() {

        CreditMessage message = mock(CreditMessage.class);

        when(message.creditId()).thenReturn(UUID.randomUUID());
        when(message.customerId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() ->
                service.notifyRenegotiated(message)
        );
    }

    @Test
    void shouldNotifyRestructured() {

        CreditMessage message = mock(CreditMessage.class);

        when(message.creditId()).thenReturn(UUID.randomUUID());
        when(message.customerId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() ->
                service.notifyRestructured(message)
        );
    }

    @Test
    void shouldNotifyPayment() {

        PaidMessage message = mock(PaidMessage.class);

        when(message.creditId()).thenReturn(UUID.randomUUID());
        when(message.customerId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() ->
                service.notifyPayment(message)
        );
    }
}