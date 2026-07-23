package com.daniela.creditflow.domain.exceptions;

import com.daniela.creditflow.domain.model.PaymentMethod;

public class UnsupportedPaymentMethodException extends BusinessRuleException {

    public UnsupportedPaymentMethodException(PaymentMethod method) {
        super("Payment method '%s' is not supported"
                .formatted(method));
    }
}
