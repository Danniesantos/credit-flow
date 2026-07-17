package com.daniela.creditflow.infrastructure.messaging.rabbitmq;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notifyApproved(CreditMessage message) {
        throw new RuntimeException("Simulando falha na notificação");

    }

    public void notifyRejected(CreditMessage message) {

        log.info("""
                        Sending rejected notification
                        
                        Credit={}
                        Customer={}
                        """,
                message.creditId(),
                message.customerId());

    }

    public void notifyContracted(CreditMessage message) {

        log.info("""
                        Sending contracted notification
                        
                        Credit={}
                        Customer={}
                        """,
                message.creditId(),
                message.customerId());

    }
}
