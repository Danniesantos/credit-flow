package com.daniela.creditflow.infrastructure.messaging.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitBindingConfig {

    @Bean
    public Binding approvedBinding(
            Queue approvedQueue,
            TopicExchange creditExchange) {

        return BindingBuilder
                .bind(approvedQueue)
                .to(creditExchange)
                .with(
                        RabbitConstants.CREDIT_APPROVED_ROUTING_KEY
                );
    }

    @Bean
    public Binding approvedRetryBinding(
            Queue approvedRetryQueue,
            TopicExchange creditRetryExchange
    ) {

        return BindingBuilder
                .bind(approvedRetryQueue)
                .to(creditRetryExchange)
                .with(RabbitConstants.CREDIT_APPROVED_ROUTING_KEY);
    }

    @Bean
    public Binding approvedDlqBinding(
            Queue approvedDlq,
            TopicExchange creditDlqExchange
    ) {

        return BindingBuilder
                .bind(approvedDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.CREDIT_APPROVED_ROUTING_KEY);

    }

    @Bean
    public Binding rejectedBinding(
            Queue rejectedQueue,
            TopicExchange creditExchange) {

        return BindingBuilder
                .bind(rejectedQueue)
                .to(creditExchange)
                .with(
                        RabbitConstants.CREDIT_REJECTED_ROUTING_KEY
                );
    }

    @Bean
    public Binding rejectedRetryBinding(
            Queue rejectedRetryQueue,
            TopicExchange creditRetryExchange
    ) {

        return BindingBuilder
                .bind(rejectedRetryQueue)
                .to(creditRetryExchange)
                .with(RabbitConstants.CREDIT_REJECTED_ROUTING_KEY);
    }

    @Bean
    public Binding rejectedDlqBinding(
            Queue rejectedDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(rejectedDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.CREDIT_REJECTED_ROUTING_KEY);
    }


    @Bean
    public Binding contractedBinding(
            Queue contractedQueue,
            TopicExchange creditExchange) {

        return BindingBuilder
                .bind(contractedQueue)
                .to(creditExchange)
                .with(
                        RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY
                );
    }

    @Bean
    public Binding contractedRetryBinding(
            Queue contractedRetryQueue,
            TopicExchange creditRetryExchange
    ) {

        return BindingBuilder
                .bind(contractedRetryQueue)
                .to(creditRetryExchange)
                .with(RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY);
    }

    @Bean
    public Binding contractedDlqBinding(
            Queue contractedDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(contractedDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentBinding(
            Queue paymentQueue,
            TopicExchange creditExchange) {

        return BindingBuilder
                .bind(paymentQueue)
                .to(creditExchange)
                .with(
                        RabbitConstants.CREDIT_PAYMENT_ROUTING_KEY
                );
    }

    @Bean
    public Binding paymentRetryBinding(
            Queue paymentRetryQueue,
            TopicExchange creditRetryExchange
    ) {

        return BindingBuilder
                .bind(paymentRetryQueue)
                .to(creditRetryExchange)
                .with(RabbitConstants.CREDIT_PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Binding paymentDlqBinding(
            Queue paymentDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(paymentDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.CREDIT_PAYMENT_ROUTING_KEY);
    }
}
