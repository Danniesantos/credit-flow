package com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher;

import com.daniela.creditflow.domain.credit.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.credit.event.CreditContractedEvent;
import com.daniela.creditflow.domain.credit.event.CreditRejectedEvent;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.mapper.CreditEventMapper;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitCreditEventPublisher implements CreditEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final CreditEventMapper mapper;

    @Override
    public void publishApproved(CreditApprovedEvent event) {
        publish(
                RabbitConstants.CREDIT_APPROVED_ROUTING_KEY,
                mapper.toMessage(event)
        );
    }

    @Override
    public void publishRejected(CreditRejectedEvent event) {
        publish(
                RabbitConstants.CREDIT_REJECTED_ROUTING_KEY,
                mapper.toMessage(event)
        );
    }

    @Override
    public void publishContracted(CreditContractedEvent event) {
        publish(
                RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY,
                mapper.toMessage(event)
        );
    }

    private void publish(
            String routingKey,
            CreditMessage message
    ) {

        rabbitTemplate.convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                routingKey,
                message
        );
    }
}
