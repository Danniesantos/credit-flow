package com.daniela.creditflow.infrastructure.messaging.rabbitmq.listener;

import com.daniela.creditflow.domain.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.event.CreditContractedEvent;
import com.daniela.creditflow.domain.event.CreditRejectedEvent;
import com.daniela.creditflow.domain.event.InstallmentPaidEvent;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher.CreditEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitEventListener {

    private final CreditEventPublisher publisher;

    @EventListener
    public void handle(CreditApprovedEvent event) {
        publisher.publishApproved(event
        );
    }

    @EventListener
    public void handle(CreditRejectedEvent event) {
        publisher.publishRejected(event);
    }

    @EventListener
    public void handle(CreditContractedEvent event) {
        publisher.publishContracted(event);
    }

    @EventListener
    public void handle(InstallmentPaidEvent event) {
        publisher.publishPayment(event);
    }

}
