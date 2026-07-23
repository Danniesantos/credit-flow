package com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher;

import com.daniela.creditflow.domain.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.event.CreditContractedEvent;
import com.daniela.creditflow.domain.event.CreditRejectedEvent;
import com.daniela.creditflow.domain.event.InstallmentPaidEvent;

public interface RabbitEventPublisher {

    void publishApproved(CreditApprovedEvent event);

    void publishRejected(CreditRejectedEvent event);

    void publishContracted(CreditContractedEvent event);

    void publishPayment(InstallmentPaidEvent event);
}
