package com.daniela.creditflow.application.installment.payment;

import com.daniela.creditflow.application.installment.payment.strategy.PaymentStrategy;
import com.daniela.creditflow.domain.exceptions.PaymentFailedException;
import com.daniela.creditflow.domain.model.PaymentMethod;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentStrategyFactory paymentFactory;

    public PaymentService(PaymentStrategyFactory paymentFactory) {
        this.paymentFactory = paymentFactory;
    }

    public PaymentResult process(PaymentMethod paymentMethod,
                                 PaymentInput input) {

        PaymentStrategy strategy =
                paymentFactory.get(paymentMethod);

        PaymentResult result =
                strategy.process(input);

        if (!result.success()) {
            throw new PaymentFailedException();
        }

        return result;
    }
}

