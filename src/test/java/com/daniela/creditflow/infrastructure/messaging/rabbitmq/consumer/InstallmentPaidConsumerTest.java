package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.application.notification.NotificationService;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InstallmentPaidConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private InstallmentPaidConsumer consumer;

    @Test
    void shouldNotifyPaymentCredit() {
        PaidMessage message = mock(PaidMessage.class);

        consumer.consume(message);

        verify(notificationService).notifyPayment(message);
    }
}