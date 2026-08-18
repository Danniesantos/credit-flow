package com.daniela.creditflow.application.installment.payment.strategy;

import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.domain.model.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class CreditCardPaymentStrategyTest {

    private final CreditCardPaymentStrategy strategy =
            new CreditCardPaymentStrategy();

    @Test
    @DisplayName("Should process credit card payment successfully")
    void shouldProcessCreditCardPaymentSuccessfully() {

        PaymentInput input =
                mock(PaymentInput.class);

        PaymentResult result =
                strategy.process(input);

        assertThat(result.success())
                .isTrue();

        assertThat(result.transactionId())
                .isNotNull()
                .isNotBlank();

        assertThat(result.paidAt())
                .isNotNull();
    }

    @Test
    @DisplayName("Should support credit card payment method")
    void shouldSupportCreditCardPaymentMethod() {

        PaymentMethod result =
                strategy.supports();

        assertThat(result)
                .isEqualTo(PaymentMethod.CREDIT_CARD);
    }
}