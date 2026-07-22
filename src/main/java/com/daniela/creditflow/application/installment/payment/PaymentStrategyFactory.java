package com.daniela.creditflow.application.installment.payment;

import com.daniela.creditflow.application.exception.UnsupportedPaymentMethodException;
import com.daniela.creditflow.application.installment.payment.strategy.PaymentStrategy;
import com.daniela.creditflow.domain.installment.valueObject.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentStrategyFactory {

    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentStrategyFactory(
            List<PaymentStrategy> strategies
    ) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::supports,
                        Function.identity()
                ));
    }

    public PaymentStrategy get(PaymentMethod method) {
        return Optional.ofNullable(strategies.get(method))
                .orElseThrow(() ->
                        new UnsupportedPaymentMethodException(method));
    }
}
