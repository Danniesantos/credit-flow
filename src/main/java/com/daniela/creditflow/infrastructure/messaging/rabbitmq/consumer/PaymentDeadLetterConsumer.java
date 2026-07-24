package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentDeadLetterConsumer {

    @RabbitListener(
            queues = RabbitConstants.CREDIT_PAYMENT_DLQ
    )
    public void consume(
            PaidMessage message,
            Message rabbitMessage
    ) {

        MessageProperties properties =
                rabbitMessage.getMessageProperties();

        log.error("""
                        [DLQ] Payment message permanently rejected
                        
                        Event={}
                        Installment={}
                        Credit={}
                        Customer={}
                        PaidAt={}
                        Queue={}
                        Headers={}
                        """,
                message.paymentEventType(),
                message.installmentId(),
                message.creditId(),
                message.customerId(),
                message.paidAt(),
                properties.getConsumerQueue(),
                properties.getHeaders()
        );
    }
}
