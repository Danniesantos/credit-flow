package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.NotificationService;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditApprovedConsumer {

    private final NotificationService notificationService;

    @RabbitListener(
            queues = RabbitConstants.CREDIT_APPROVED_QUEUE
    )
    public void consume(CreditMessage message) {

        log.info(
                "[RABBITMQ] Received event={} credit={}",
                message.eventType(),
                message.creditId()
        );

        notificationService.notifyApproved(message);
    }
}
