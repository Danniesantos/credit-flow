package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.application.notification.NotificationService;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreditRenegotiatedConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CreditRenegotiatedConsumer consumer;

    @Test
    void shouldNotifyRenegotiatedCredit() {
        CreditMessage message = mock(CreditMessage.class);

        consumer.consume(message);

        verify(notificationService).notifyRenegotiated(message);
    }
}