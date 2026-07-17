package com.daniela.creditflow.infrastructure.messaging.rabbitmq.consumer;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.NotificationService;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreditContractedConsumer {

    private final NotificationService notificationService;

    @RabbitListener(
            queues = RabbitConstants.CREDIT_CONTRACTED_QUEUE
    )
    public void consume(CreditMessage message,
                        Channel channel,
                        Message rabbitMessage
    ) throws IOException {

        long deliveryTag =
                rabbitMessage
                        .getMessageProperties()
                        .getDeliveryTag();

        try {
            log.info("[RABBITMQ] Received event={} credit={}",
                    message.eventType(),
                    message.creditId()
            );

            log.info(
                    "Headers={}",
                    rabbitMessage.getMessageProperties()
                            .getHeaders()
            );

            notificationService.notifyContracted(message);

            channel.basicAck(
                    deliveryTag,
                    false
            );

            log.info(
                    "[RABBITMQ] ACK sent for credit={}",
                    message.creditId()
            );
        } catch (Exception ex) {
            log.error(
                    "[RABBITMQ] Error processing credit={}",
                    message.creditId(),
                    ex
            );

            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );
        }

    }

    private boolean exceededRetryLimit(Message message) {

        Object xDeath =
                message.getMessageProperties()
                        .getHeaders()
                        .get("x-death");

        if (xDeath instanceof List<?> deaths) {

            Map<?, ?> firstDeath =
                    (Map<?, ?>) deaths.get(0);

            Long count =
                    (Long) firstDeath.get("count");

            return count >= 3;
        }

        return false;
    }
}
