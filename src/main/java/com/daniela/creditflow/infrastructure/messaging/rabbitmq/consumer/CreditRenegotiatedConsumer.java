package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.application.notification.NotificationService;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreditRenegotiatedConsumer {

    private final NotificationService notificationService;

    @RabbitListener(
            queues = RabbitConstants.CREDIT_RENEGOTIATED_QUEUE
    )
    public void consume(CreditMessage message) {

        log.info(
                "[RABBITMQ] Received event={} credit={} customer={}",
                message.eventType(),
                message.creditId(),
                message.customerId()
        );

        notificationService.notifyRenegotiated(message);
    }
}
