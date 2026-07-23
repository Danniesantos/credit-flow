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
    public Queue approvedRetryQueue() {

        return retryQueue(
                RabbitConstants.CREDIT_APPROVED_RETRY_QUEUE,
                RabbitConstants.CREDIT_APPROVED_ROUTING_KEY
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
    public Queue rejectedRetryQueue() {

        return retryQueue(RabbitConstants.CREDIT_REJECTED_RETRY_QUEUE,
                RabbitConstants.CREDIT_REJECTED_ROUTING_KEY
        );
    }

    @Bean
    public Queue contractedQueue() {

        return queue(
                RabbitConstants.CREDIT_CONTRACTED_QUEUE
        );
    }

    @Bean
    public Queue contractedRetryQueue() {

        return retryQueue(
                RabbitConstants.CREDIT_CONTRACTED_RETRY_QUEUE,
                RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY
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
    public Queue paymentRetryQueue() {

        return retryQueue(
                RabbitConstants.CREDIT_PAYMENT_RETRY_QUEUE,
                RabbitConstants.CREDIT_PAYMENT_ROUTING_KEY
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
                .withArgument(
                        "x-dead-letter-exchange",
                        RabbitConstants.CREDIT_RETRY_EXCHANGE
                )
                .build();
    }

    private Queue retryQueue(String queueName, String routingKey) {
        return QueueBuilder
                .durable(queueName)
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", RabbitConstants.CREDIT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", routingKey)
                .build();
    }

    private Queue dlq(String queueName) {
        return QueueBuilder
                .durable(queueName)
                .build();
    }
}
