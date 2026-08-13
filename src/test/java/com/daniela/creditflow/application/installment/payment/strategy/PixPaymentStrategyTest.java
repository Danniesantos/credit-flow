package com.daniela.creditflow.application.installment.payment.strategy;

import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.domain.model.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class PixPaymentStrategyTest {

    private final PixPaymentStrategy strategy =
            new PixPaymentStrategy();

    @Test
    @DisplayName("Should process PIX payment successfully")
    void shouldProcessPixPaymentSuccessfully() {

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
    @DisplayName("Should support PIX payment method")
    void shouldSupportPixPaymentMethod() {

        PaymentMethod result =
                strategy.supports();

        assertThat(result)
                .isEqualTo(PaymentMethod.PIX);
    }
}