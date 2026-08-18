package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.service.CreditInstallmentService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.event.CreditRestructuredEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.InstallmentTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private RestructureCreditUseCase useCase;

    @Test
    @DisplayName("Should restructure credit and publish event")
    void shouldRestructureCredit() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        CreditId creditId =
                credit.getId();

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(12);

        List<Installment> installments =
                InstallmentTestFactory.installments(
                        creditId,
                        12
                );

        when(service.findCredit(creditId))
                .thenReturn(credit);

        when(creditInstallmentService.generate(
                credit,
                input.installmentsQuantity()
        )).thenReturn(installments);

        useCase.execute(
                creditId,
                input
        );

        assertThat(credit.getInstallments())
                .hasSize(installments.size());

        verify(creditRepository)
                .save(credit);

        verify(eventPublisher)
                .publishEvent(
                        any(CreditRestructuredEvent.class)
                );
    }
}