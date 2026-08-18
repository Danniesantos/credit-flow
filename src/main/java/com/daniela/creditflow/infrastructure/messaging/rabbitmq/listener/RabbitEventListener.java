package com.daniela.creditflow.infrastructure.messaging.rabbitmq.listener;

import com.daniela.creditflow.domain.event.*;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher.RabbitEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitEventListener {

    private final RabbitEventPublisher publisher;

    @EventListener
    public void handle(CreditApprovedEvent event) {
        publisher.publishApproved(event);
    }

    @EventListener
    public void handle(CreditRejectedEvent event) {
        publisher.publishRejected(event);
    }

    @EventListener
    public void handle(CreditCanceledEvent event) {
        publisher.publishCanceled(event);
    }

    @EventListener
    public void handle(CreditContractedEvent event) {
        publisher.publishContracted(event);
    }

    @EventListener
    public void handle(CreditRenegotiatedEvent event) {
        publisher.publishRenegotiated(event);
    }

    @EventListener
    public void handle(CreditRestructuredEvent event) {
        publisher.publishRestructured(event);
    }

    @EventListener
    public void handle(InstallmentPaidEvent event) {
        publisher.publishPayment(event);
    }

}
