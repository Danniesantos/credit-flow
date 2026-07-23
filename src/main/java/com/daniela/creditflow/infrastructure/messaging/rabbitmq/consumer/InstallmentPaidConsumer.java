package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.application.notification.NotificationService;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstallmentPaidConsumer {

    private final NotificationService notificationService;

    @RabbitListener(
            queues = RabbitConstants.CREDIT_PAYMENT_QUEUE
    )
    public void consume(PaidMessage message) {

        log.info(
                "[RABBITMQ] Received event={} credit={} installment={} customer={}",
                message.paymentEventType(),
                message.creditId(),
                message.installmentId(),
                message.customerId()

        );

        notificationService.notifyPayment(message);
    }
}
