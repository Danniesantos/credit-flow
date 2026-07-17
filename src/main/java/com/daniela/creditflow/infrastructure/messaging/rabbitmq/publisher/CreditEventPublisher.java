package com.daniela.creditflow.infrastructure.messaging.rabbitmq.publisher;

import com.daniela.creditflow.domain.credit.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.credit.event.CreditContractedEvent;
import com.daniela.creditflow.domain.credit.event.CreditRejectedEvent;

public interface CreditEventPublisher {

    void publishApproved(CreditApprovedEvent event);

    void publishRejected(CreditRejectedEvent event);

    void publishContracted(CreditContractedEvent event);
}
