package com.daniela.creditflow.infrastructure.messaging.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConfig {

    @Bean
    public Queue approvedQueue() {

        return queue(
                RabbitConstants.CREDIT_APPROVED_QUEUE
        );
    }

    @Bean
    public Queue approvedDlq() {

        return dlq(
                RabbitConstants.CREDIT_APPROVED_DLQ
        );
    }

    @Bean
    public Queue rejectedQueue() {

        return queue(
                RabbitConstants.CREDIT_REJECTED_QUEUE
        );
    }

    @Bean
    public Queue rejectedDlq() {

        return dlq(
                RabbitConstants.CREDIT_REJECTED_DLQ
        );
    }

    @Bean
    public Queue canceledQueue() {

        return queue(
                RabbitConstants.CREDIT_CANCELED_QUEUE
        );
    }

    @Bean
    public Queue canceledDlq() {

        return dlq(
                RabbitConstants.CREDIT_CANCELED_DLQ
        );
    }

    @Bean
    public Queue contractedQueue() {

        return queue(
                RabbitConstants.CREDIT_CONTRACTED_QUEUE
        );
    }

    @Bean
    public Queue contractedDlq() {

        return dlq(
                RabbitConstants.CREDIT_CONTRACTED_DLQ
        );
    }

    @Bean
    public Queue paymentQueue() {

        return queue(
                RabbitConstants.CREDIT_PAYMENT_QUEUE
        );
    }

    @Bean
    public Queue paymentDlq() {

        return dlq(
                RabbitConstants.CREDIT_PAYMENT_DLQ
        );
    }

    private Queue queue(String queueName) {
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    private Queue dlq(String queueName) {
        return QueueBuilder
                .durable(queueName)
                .build();
    }
}
