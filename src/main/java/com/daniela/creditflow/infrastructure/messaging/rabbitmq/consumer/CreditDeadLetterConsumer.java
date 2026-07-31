package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditDeadLetterConsumer {

    @RabbitListener(
            queues = {
                    RabbitConstants.CREDIT_APPROVED_DLQ,
                    RabbitConstants.CREDIT_REJECTED_DLQ,
                    RabbitConstants.CREDIT_CANCELED_DLQ,
                    RabbitConstants.CREDIT_CONTRACTED_DLQ,
                    RabbitConstants.CREDIT_RENEGOTIATED_DLQ,
                    RabbitConstants.CREDIT_RESTRUCTURED_DLQ
            }
    )
    public void consume(
            CreditMessage message,
            Message rabbitMessage
    ) {

        MessageProperties properties =
                rabbitMessage.getMessageProperties();

        log.error("""
                        [DLQ] Message permanently rejected
                        
                        Event={}
                        Credit={}
                        Customer={}
                        Queue={}
                        Headers={}
                        """,
                message.eventType(),
                message.creditId(),
                message.customerId(),
                properties.getConsumerQueue(),
                properties.getHeaders()
        );
    }
}
