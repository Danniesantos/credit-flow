package com.daniela.creditflow.application.notification;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class NotificationServiceTest {

    private final NotificationService service = new NotificationService();

    @Test
    void shouldNotifyApproved() {
        service.notifyApproved(mock(CreditMessage.class));
    }

    @Test
    void shouldNotifyRejected() {
        service.notifyRejected(mock(CreditMessage.class));
    }

    @Test
    void shouldNotifyCanceled() {
        service.notifyCanceled(mock(CreditMessage.class));
    }

    @Test
    void shouldNotifyContracted() {
        service.notifyContracted(mock(CreditMessage.class));
    }

    @Test
    void shouldNotifyRenegotiated() {
        service.notifyRenegotiated(mock(CreditMessage.class));
    }

    @Test
    void shouldNotifyRestructured() {
        service.notifyRestructured(mock(CreditMessage.class));
    }

    @Test
    void shouldNotifyPayment() {
        service.notifyPayment(mock(PaidMessage.class));
    }
}