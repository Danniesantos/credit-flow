package com.daniela.creditflow.application.notification;

import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.CreditMessage;
import com.daniela.creditflow.infrastructure.messaging.rabbitmq.message.PaidMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notifyApproved(CreditMessage message) {

        log.info("""
                        Sending approved notification
                        
                        Credit={}
                        Customer={}
                        """,
                message.creditId(),
                message.customerId());

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

    public void notifyPayment(PaidMessage message) {

        log.info("""
                        Sending Payment notification
                        
                        Credit={}
                        Customer={}
                        """,
                message.creditId(),
                message.customerId());

    }
}
