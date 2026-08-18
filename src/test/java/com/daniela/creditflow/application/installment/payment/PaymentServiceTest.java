package com.daniela.creditflow.application.installment.payment;

import com.daniela.creditflow.application.installment.payment.strategy.PaymentStrategy;
import com.daniela.creditflow.domain.exceptions.PaymentFailedException;
import com.daniela.creditflow.domain.model.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentStrategyFactory paymentFactory;
    @Mock
    private PaymentStrategy strategy;
    @Mock
    private PaymentInput input;
    @InjectMocks
    private PaymentService service;

    @Test
    @DisplayName("Should process payment successfully")
    void shouldProcessPaymentSuccessfully() {

        PaymentMethod paymentMethod =
                PaymentMethod.PIX;

        PaymentResult expected =
                mock(PaymentResult.class);

        when(paymentFactory.get(paymentMethod))
                .thenReturn(strategy);

        when(strategy.process(input))
                .thenReturn(expected);

        when(expected.success())
                .thenReturn(true);

        PaymentResult result =
                service.process(
                        paymentMethod,
                        input
                );

        assertThat(result)
                .isEqualTo(expected);

        verify(paymentFactory)
                .get(paymentMethod);

        verify(strategy)
                .process(input);
    }

    @Test
    @DisplayName("Should throw exception when payment fails")
    void shouldThrowExceptionWhenPaymentFails() {

        PaymentMethod paymentMethod =
                PaymentMethod.PIX;

        PaymentResult result =
                mock(PaymentResult.class);

        when(paymentFactory.get(paymentMethod))
                .thenReturn(strategy);

        when(strategy.process(input))
                .thenReturn(result);

        when(result.success())
                .thenReturn(false);

        assertThatThrownBy(() ->
                service.process(
                        paymentMethod,
                        input
                )
        )
                .isInstanceOf(PaymentFailedException.class);

        verify(paymentFactory)
                .get(paymentMethod);

        verify(strategy)
                .process(input);
    }
}