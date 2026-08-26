package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.service.CreditInstallmentService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.event.CreditRenegotiatedEvent;
import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.InstallmentTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenegotiateCreditUseCaseTest {

    @Mock
    private CreditService creditService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditInstallmentService creditInstallmentService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private RenegotiateCreditUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new RenegotiateCreditUseCase(
                creditService,
                creditRepository,
                creditInstallmentService,
                eventPublisher,
                TestConstants.FIXED_CLOCK
        );
    }

    @Test
    @DisplayName("Should renegotiate credit successfully")
    void shouldRenegotiateCreditSuccessfully() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        List<Installment> newInstallments =
                InstallmentTestFactory.installments(
                        credit.getId(),
                        credit.nextInstallmentNumber(),
                        6
                );

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(6);

        when(creditService.findCredit(credit.getId()))
                .thenReturn(credit);

        when(creditInstallmentService.generate(credit, 6))
                .thenReturn(newInstallments);

        useCase.execute(credit.getId(), input);

        ArgumentCaptor<CreditRenegotiatedEvent> eventCaptor =
                ArgumentCaptor.forClass(CreditRenegotiatedEvent.class);

        verify(eventPublisher)
                .publishEvent(eventCaptor.capture());

        CreditRenegotiatedEvent event =
                eventCaptor.getValue();

        assertThat(event.creditId())
                .isEqualTo(credit.getId());

        assertThat(event.customerId())
                .isEqualTo(credit.getCustomerId());

        verify(creditService)
                .findCredit(credit.getId());

        verify(creditInstallmentService)
                .generate(credit, 6);

        verify(creditRepository)
                .save(credit);

    }

    @Test
    @DisplayName("Should not generate installments when credit cannot be renegotiated")
    void shouldNotGenerateInstallmentsWhenCreditCannotBeRenegotiated() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(6);

        when(creditService.findCredit(credit.getId()))
                .thenReturn(credit);

        assertThatThrownBy(() ->
                useCase.execute(credit.getId(), input)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Credit cannot be renegotiated");

        verify(creditInstallmentService, never())
                .generate(any(), anyInt());

        verify(creditRepository, never())
                .save(any());

        verify(eventPublisher, never())
                .publishEvent(any());
    }

    @Test
    @DisplayName("Should not renegotiate when credit is not found")
    void shouldNotRenegotiateWhenCreditNotFound() {

        CreditId creditId =
                new CreditId(UUID.randomUUID());

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(6);

        when(creditService.findCredit(creditId))
                .thenThrow(new CreditNotFoundException());

        assertThatThrownBy(() ->
                useCase.execute(creditId, input)
        )
                .isInstanceOf(CreditNotFoundException.class);

        verify(creditInstallmentService, never())
                .generate(any(), anyInt());

        verify(creditRepository, never())
                .save(any());

        verify(eventPublisher, never())
                .publishEvent(any());
    }
}