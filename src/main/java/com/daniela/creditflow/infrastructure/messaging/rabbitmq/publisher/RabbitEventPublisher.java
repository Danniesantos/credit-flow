package com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher;

import com.daniela.creditflow.domain.event.*;

public interface RabbitEventPublisher {

    void publishApproved(CreditApprovedEvent event);

    void publishRejected(CreditRejectedEvent event);

    void publishCanceled(CreditCanceledEvent event);

    void publishContracted(CreditContractedEvent event);

    void publishPayment(InstallmentPaidEvent event);
}
