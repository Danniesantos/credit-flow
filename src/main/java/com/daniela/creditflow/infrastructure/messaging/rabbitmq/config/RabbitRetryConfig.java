package com.daniela.creditflow.infrastructure.messaging.rabbitmq.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
@RequiredArgsConstructor
public class RabbitRetryConfig {

    private final RabbitTemplate rabbitTemplate;

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {

        RepublishMessageRecoverer recoverer =
                new RepublishMessageRecoverer(
                        rabbitTemplate,
                        RabbitConstants.CREDIT_DLQ_EXCHANGE,
                        RabbitConstants.CREDIT_APPROVED_ROUTING_KEY
                );

        return RetryInterceptorBuilder
                .stateless()
                .maxAttempts(3)
                .backOffOptions(
                        1000,
                        2.0,
                        10000
                )
                .recoverer(recoverer)
                .build();
    }
}
