package com.daniela.creditflow.application.installment.payment.strategy;

import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.domain.installment.valueObject.PaymentMethod;

public interface PaymentStrategy {

    PaymentResult process(PaymentInput input);

    PaymentMethod supports();
}
