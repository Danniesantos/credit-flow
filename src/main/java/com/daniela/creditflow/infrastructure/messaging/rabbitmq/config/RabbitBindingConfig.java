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
    public Binding approvedDlqBinding(
            Queue approvedDlq,
            TopicExchange creditDlqExchange
    ) {

        return BindingBuilder
                .bind(approvedDlq)
                .to(creditDlqExchange)
                .with(
                        RabbitConstants.ERROR_ROUTING_PREFIX
                                + RabbitConstants.CREDIT_APPROVED_ROUTING_KEY);

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
    public Binding rejectedDlqBinding(
            Queue rejectedDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(rejectedDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.ERROR_ROUTING_PREFIX
                        + RabbitConstants.CREDIT_REJECTED_ROUTING_KEY);
    }

    @Bean
    public Binding canceledBinding(
            Queue canceledQueue,
            TopicExchange creditExchange) {

        return BindingBuilder
                .bind(canceledQueue)
                .to(creditExchange)
                .with(
                        RabbitConstants.CREDIT_CANCELED_ROUTING_KEY
                );
    }

    @Bean
    public Binding canceledDlqBinding(
            Queue canceledDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(canceledDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.ERROR_ROUTING_PREFIX
                        + RabbitConstants.CREDIT_CANCELED_ROUTING_KEY);
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
    public Binding contractedDlqBinding(
            Queue contractedDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(contractedDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.ERROR_ROUTING_PREFIX
                        + RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY);
    }

    @Bean
    public Binding renegotiatedBinding(
            Queue renegotiatedQueue,
            TopicExchange creditExchange) {

        return BindingBuilder
                .bind(renegotiatedQueue)
                .to(creditExchange)
                .with(
                        RabbitConstants.CREDIT_RENEGOTIATED_ROUTING_KEY
                );
    }

    @Bean
    public Binding renegotiatedDlqBinding(
            Queue renegotiatedDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(renegotiatedDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.ERROR_ROUTING_PREFIX
                        + RabbitConstants.CREDIT_RENEGOTIATED_ROUTING_KEY);
    }

    @Bean
    public Binding restructuredBinding(
            Queue restructuredQueue,
            TopicExchange creditExchange) {

        return BindingBuilder
                .bind(restructuredQueue)
                .to(creditExchange)
                .with(
                        RabbitConstants.CREDIT_RESTRUCTURED_ROUTING_KEY
                );
    }

    @Bean
    public Binding restructuredDlqBinding(
            Queue restructuredDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(restructuredDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.ERROR_ROUTING_PREFIX
                        + RabbitConstants.CREDIT_RESTRUCTURED_ROUTING_KEY);
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
    public Binding paymentDlqBinding(
            Queue paymentDlq,
            TopicExchange creditDlqExchange) {

        return BindingBuilder.bind(paymentDlq)
                .to(creditDlqExchange)
                .with(RabbitConstants.ERROR_ROUTING_PREFIX
                        + RabbitConstants.CREDIT_APPROVED_ROUTING_KEY);
    }
}
