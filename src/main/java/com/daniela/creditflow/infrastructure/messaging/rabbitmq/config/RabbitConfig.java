package com.daniela.creditflow.infrastructure.messaging.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        return factory;
    }

    @Bean
    public TopicExchange creditExchange() {

        return new TopicExchange(
                RabbitConstants
                        .CREDIT_EXCHANGE
        );
    }

    @Bean
    public Queue approvedQueue() {

        return QueueBuilder
                .durable(
                        RabbitConstants.CREDIT_APPROVED_QUEUE
                )
                .withArgument(
                        "x-dead-letter-exchange",
                        RabbitConstants.CREDIT_RETRY_EXCHANGE
                )
                .build();
    }

    @Bean
    public Queue approvedRetryQueue() {

        return QueueBuilder
                .durable(
                        RabbitConstants.CREDIT_APPROVED_RETRY_QUEUE
                )
                .withArgument(
                        "x-message-ttl",
                        5000
                )
                .withArgument(
                        "x-dead-letter-exchange",
                        RabbitConstants.CREDIT_EXCHANGE
                )
                .withArgument(
                        "x-dead-letter-routing-key",
                        RabbitConstants.CREDIT_APPROVED_ROUTING_KEY
                )
                .build();
    }

    @Bean
    public Queue approvedDlq() {

        return QueueBuilder
                .durable(
                        RabbitConstants.CREDIT_APPROVED_DLQ
                )
                .build();
    }

    @Bean
    public Binding approvedBinding(Queue approvedQueue,
                                   TopicExchange creditExchange) {

        return binding(
                approvedQueue,
                creditExchange,
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
                .with(RabbitConstants.CREDIT_APPROVED_ROUTING_KEY);

    }

    @Bean
    public Queue rejectedQueue() {

        return durableQueue(
                RabbitConstants.CREDIT_REJECTED_QUEUE
        );
    }

    @Bean
    public Queue contractedQueue() {

        return durableQueue(
                RabbitConstants.CREDIT_CONTRACTED_QUEUE
        );
    }


    @Bean
    public Binding rejectedBinding(Queue rejectedQueue,
                                   TopicExchange creditExchange) {

        return binding(
                rejectedQueue,
                creditExchange,
                RabbitConstants.CREDIT_REJECTED_ROUTING_KEY
        );
    }

    @Bean
    public Binding contractedBinding(Queue contractedQueue,
                                     TopicExchange creditExchange) {

        return binding(
                contractedQueue,
                creditExchange,
                RabbitConstants.CREDIT_CONTRACTED_ROUTING_KEY
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
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    private Queue durableQueue(String queueName) {
        return new Queue(queueName, true);
    }

    private Binding binding(Queue queue,
                            TopicExchange exchange,
                            String routingKey) {

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);

    }

}
