package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.service.CreditInstallmentService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.event.CreditRestructuredEvent;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestructureCreditUseCaseTest {

    @Mock
    private CreditService service;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private CreditInstallmentService creditInstallmentService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private RestructureCreditUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new RestructureCreditUseCase(
                service,
                creditRepository,
                creditInstallmentService,
                eventPublisher,
                TestConstants.FIXED_CLOCK
        );
    }

    @Test
    @DisplayName("Should restructure credit successfully")
    void shouldRestructureCreditSuccessfully() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        CreditId creditId =
                credit.getId();

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(12);

        List<Installment> installments =
                InstallmentTestFactory.installments(
                        creditId,
                        credit.nextInstallmentNumber(),
                        12
                );

        when(service.findCredit(creditId))
                .thenReturn(credit);

        when(creditInstallmentService.generate(
                credit,
                input.installmentsQuantity()
        ))
                .thenReturn(installments);

        useCase.execute(
                creditId,
                input
        );

        assertThat(credit.getInstallments())
                .hasSize(12);

        verify(service)
                .findCredit(creditId);

        verify(creditInstallmentService)
                .generate(
                        credit,
                        input.installmentsQuantity()
                );

        verify(creditRepository)
                .save(credit);

        ArgumentCaptor<CreditRestructuredEvent> eventCaptor =
                ArgumentCaptor.forClass(
                        CreditRestructuredEvent.class
                );

        verify(eventPublisher)
                .publishEvent(eventCaptor.capture());

        CreditRestructuredEvent event =
                eventCaptor.getValue();

        assertThat(event.creditId())
                .isEqualTo(creditId);

        assertThat(event.customerId())
                .isEqualTo(credit.getCustomerId());
    }

    @Test
    @DisplayName("Should not restructure paid off credit")
    void shouldNotRestructurePaidOffCredit() {

        Credit credit =
                CreditTestFactory.paidOffCredit();

        CreditId creditId =
                credit.getId();

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(12);

        List<Installment> installments =
                InstallmentTestFactory.installments(
                        creditId,
                        credit.nextInstallmentNumber(),
                        12
                );

        when(service.findCredit(creditId))
                .thenReturn(credit);

        when(creditInstallmentService.generate(
                credit,
                input.installmentsQuantity()
        ))
                .thenReturn(installments);

        assertThatThrownBy(() ->
                useCase.execute(creditId, input)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Credit cannot be restructured");

        verify(creditRepository, never())
                .save(any());

        verify(eventPublisher, never())
                .publishEvent(any());
    }

    @Test
    @DisplayName("Should not restructure when credit is not found")
    void shouldNotRestructureWhenCreditNotFound() {

        CreditId creditId =
                new CreditId(UUID.randomUUID());

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(12);

        when(service.findCredit(creditId))
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