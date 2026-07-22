package com.daniela.creditflow.application.exception;

import com.daniela.creditflow.domain.installment.valueObject.PaymentMethod;

public class UnsupportedPaymentMethodException extends RuntimeException {

    public UnsupportedPaymentMethodException(PaymentMethod method) {
        super("Payment method '%s' is not supported"
                .formatted(method));
    }
}
