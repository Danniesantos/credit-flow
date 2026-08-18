package com.daniela.creditflow.application.installment.usecase;

import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.application.installment.payment.PaymentService;
import com.daniela.creditflow.domain.event.InstallmentPaidEvent;
import com.daniela.creditflow.domain.exceptions.PaymentFailedException;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayInstallmentUseCaseTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditService creditService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PayInstallmentUseCase useCase;

    @Test
    @DisplayName("Should pay installment and publish event")
    void shouldPayInstallmentAndPublishEvent() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        Installment installment =
                credit.getInstallments().getFirst();

        PaymentInstallmentInput input =
                new PaymentInstallmentInput(
                        credit.getId().value(),
                        installment.getId().value(),
                        PaymentMethod.PIX
                );

        PaymentResult paymentResult =
                new PaymentResult(
                        true,
                        "transaction-123",
                        TestConstants.PAID_AT
                );

        when(creditService.findCredit(
                new CreditId(input.creditId())
        )).thenReturn(credit);

        when(paymentService.process(
                eq(PaymentMethod.PIX),
                any(PaymentInput.class)
        )).thenReturn(paymentResult);

        useCase.execute(input);

        assertThat(installment.getStatus())
                .isEqualTo(InstallmentStatus.PAID);

        assertThat(installment.getPaymentMethod())
                .isEqualTo(PaymentMethod.PIX);

        assertThat(installment.getPaidAt())
                .isEqualTo(TestConstants.PAID_AT);

        verify(creditRepository)
                .save(credit);

        verify(eventPublisher)
                .publishEvent(
                        any(InstallmentPaidEvent.class)
                );
    }

    @Test
    @DisplayName("Should process payment using installment amount")
    void shouldProcessPaymentUsingInstallmentAmount() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        Installment installment =
                credit.getInstallments().getFirst();

        PaymentInstallmentInput input =
                new PaymentInstallmentInput(
                        credit.getId().value(),
                        installment.getId().value(),
                        PaymentMethod.PIX
                );

        PaymentResult paymentResult =
                new PaymentResult(
                        true,
                        "transaction-123",
                        TestConstants.PAID_AT
                );

        when(creditService.findCredit(
                new CreditId(input.creditId())
        )).thenReturn(credit);

        when(paymentService.process(
                eq(PaymentMethod.PIX),
                any(PaymentInput.class)
        )).thenReturn(paymentResult);

        useCase.execute(input);

        ArgumentCaptor<PaymentInput> captor =
                ArgumentCaptor.forClass(PaymentInput.class);

        verify(paymentService)
                .process(
                        eq(PaymentMethod.PIX),
                        captor.capture()
                );

        assertThat(captor.getValue().amount())
                .isEqualTo(
                        installment.getAmount().value()
                );
    }

    @Test
    @DisplayName("Should not save credit when payment fails")
    void shouldNotSaveCreditWhenPaymentFails() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        Installment installment =
                credit.getInstallments().getFirst();

        PaymentInstallmentInput input =
                new PaymentInstallmentInput(
                        credit.getId().value(),
                        installment.getId().value(),
                        PaymentMethod.PIX
                );

        when(creditService.findCredit(
                new CreditId(input.creditId())
        )).thenReturn(credit);

        when(paymentService.process(
                eq(PaymentMethod.PIX),
                any(PaymentInput.class)
        )).thenThrow(
                new PaymentFailedException()
        );

        assertThatThrownBy(() ->
                useCase.execute(input)
        )
                .isInstanceOf(PaymentFailedException.class);

        verify(creditRepository, never())
                .save(any());

        verify(eventPublisher, never())
                .publishEvent(any());
    }
}