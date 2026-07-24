package com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher;

import com.daniela.creditflow.domain.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.event.CreditContractedEvent;
import com.daniela.creditflow.domain.event.CreditRejectedEvent;
import com.daniela.creditflow.domain.event.InstallmentPaidEvent;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.config.RabbitConstants;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.mapper.RabbitEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitCreditEventPublisher implements RabbitEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitEventMapper mapper;

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

    @Override
    public void publishPayment(InstallmentPaidEvent event) {
        publish(
                RabbitConstants.CREDIT_PAYMENT_ROUTING_KEY,
                mapper.toMessage(event)
        );
    }

    private void publish(String routingKey, Object message) {
        rabbitTemplate.convertAndSend(
                RabbitConstants.CREDIT_EXCHANGE,
                routingKey,
                message
        );
    }
}
