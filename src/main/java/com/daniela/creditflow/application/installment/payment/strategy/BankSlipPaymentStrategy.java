package com.daniela.creditflow.application.installment.payment.strategy;

import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.domain.model.PaymentMethod;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class BankSlipPaymentStrategy implements PaymentStrategy {


    @Override
    public PaymentResult process(PaymentInput input) {
        return new PaymentResult(true,
                UUID.randomUUID().toString(),
                Instant.now());
    }

    @Override
    public PaymentMethod supports() {
        return PaymentMethod.BANK_SLIP;
    }
}
