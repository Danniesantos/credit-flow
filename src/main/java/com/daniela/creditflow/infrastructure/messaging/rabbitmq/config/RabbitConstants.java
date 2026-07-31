package com.daniela.creditflow.infrastructure.messaging.rabbitmq.config;

public class RabbitConstants {

    public static final String CREDIT_EXCHANGE = "credit.exchange";

    public static final String CREDIT_APPROVED_QUEUE = "credit.approved.queue";
    public static final String CREDIT_REJECTED_QUEUE = "credit.rejected.queue";
    public static final String CREDIT_CANCELED_QUEUE = "credit.canceled.queue";
    public static final String CREDIT_CONTRACTED_QUEUE = "credit.contracted.queue";
    public static final String CREDIT_RENEGOTIATED_QUEUE = "credit.renegotiated.queue";
    public static final String CREDIT_RESTRUCTURED_QUEUE = "credit.restructured.queue";
    public static final String CREDIT_PAYMENT_QUEUE = "credit.payment.queue";

    public static final String CREDIT_APPROVED_ROUTING_KEY = "credit.approved";
    public static final String CREDIT_REJECTED_ROUTING_KEY = "credit.rejected";
    public static final String CREDIT_CANCELED_ROUTING_KEY = "credit.canceled";
    public static final String CREDIT_CONTRACTED_ROUTING_KEY = "credit.contracted";
    public static final String CREDIT_RENEGOTIATED_ROUTING_KEY = "credit.renegotiated";
    public static final String CREDIT_RESTRUCTURED_ROUTING_KEY = "credit.restructured";
    public static final String CREDIT_PAYMENT_ROUTING_KEY = "credit.payment";

    public static final String CREDIT_RETRY_EXCHANGE = "credit.retry.exchange";

    public static final String CREDIT_APPROVED_DLQ = "credit.approved.dlq";
    public static final String CREDIT_REJECTED_DLQ = "credit.rejected.dlq";
    public static final String CREDIT_CANCELED_DLQ = "credit.canceled.dlq";
    public static final String CREDIT_CONTRACTED_DLQ = "credit.contracted.dlq";
    public static final String CREDIT_RENEGOTIATED_DLQ = "credit.renegotiated.dlq";
    public static final String CREDIT_RESTRUCTURED_DLQ = "credit.restructured.dlq";

    public static final String CREDIT_PAYMENT_DLQ = "credit.payment.dlq";


    public static final String CREDIT_DLQ_EXCHANGE = "credit.dlq.exchange";
}
