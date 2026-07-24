package com.daniela.creditflow.infrastructure.messaging.rabbitmq.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitExchangeConfig {

    @Bean
    public TopicExchange creditExchange() {

        return new TopicExchange(
                RabbitConstants.CREDIT_EXCHANGE
        );
    }

    @Bean
    public TopicExchange creditRetryExchange() {

        return new TopicExchange(
                RabbitConstants.CREDIT_RETRY_EXCHANGE
        );
    }

    @Bean
    public TopicExchange creditDlqExchange() {

        return new TopicExchange(
                RabbitConstants.CREDIT_DLQ_EXCHANGE
        );
    }
}
