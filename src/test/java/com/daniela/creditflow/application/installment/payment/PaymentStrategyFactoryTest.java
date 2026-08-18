package com.daniela.creditflow.application.installment.payment;

import com.daniela.creditflow.application.installment.payment.strategy.PaymentStrategy;
import com.daniela.creditflow.domain.exceptions.UnsupportedPaymentMethodException;
import com.daniela.creditflow.domain.model.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentStrategyFactoryTest {

    private PaymentStrategyFactory factory;

    @Mock
    private PaymentStrategy pixStrategy;

    @Mock
    private PaymentStrategy bankSlipStrategy;

    @BeforeEach
    void setUp() {

        when(pixStrategy.supports())
                .thenReturn(PaymentMethod.PIX);

        when(bankSlipStrategy.supports())
                .thenReturn(PaymentMethod.BANK_SLIP);

        factory =
                new PaymentStrategyFactory(
                        List.of(
                                pixStrategy,
                                bankSlipStrategy
                        )
                );
    }

    @Test
    @DisplayName("Should return strategy for payment method")
    void shouldReturnStrategyForPaymentMethod() {

        PaymentStrategy result =
                factory.get(PaymentMethod.PIX);

        assertThat(result)
                .isSameAs(pixStrategy);
    }

    @Test
    @DisplayName("Should throw exception for unsupported payment method")
    void shouldThrowExceptionForUnsupportedPaymentMethod() {

        assertThatThrownBy(() ->
                factory.get(PaymentMethod.CREDIT_CARD)
        )
                .isInstanceOf(
                        UnsupportedPaymentMethodException.class
                );
    }
}