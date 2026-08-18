package com.daniela.creditflow.application.installment.payment.strategy;

import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.domain.model.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class DebitAccountPaymentStrategyTest {

    private final DebitAccountPaymentStrategy strategy =
            new DebitAccountPaymentStrategy();

    @Test
    @DisplayName("Should process debit account payment successfully")
    void shouldProcessDebitAccountPaymentSuccessfully() {

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
    @DisplayName("Should support debit account payment method")
    void shouldSupportDebitAccountPaymentMethod() {

        PaymentMethod result =
                strategy.supports();

        assertThat(result)
                .isEqualTo(PaymentMethod.DEBIT_ACCOUNT);
    }
}