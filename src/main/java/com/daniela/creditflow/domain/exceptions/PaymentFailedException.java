package com.daniela.creditflow.domain.exceptions;

public class PaymentFailedException extends BusinessRuleException {

    public PaymentFailedException() {
        super("Payment failed");
    }
}
