package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.installment.factory.InstallmentFactory;
import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.event.CreditContractedEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.Money;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.InstallmentTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractCreditUseCaseTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditCalculationService calculationService;

    @Mock
    private CreditService creditService;

    @Mock
    private InstallmentFactory installmentFactory;

    @Mock
    private DueDatePolicy dueDatePolicy;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private ContractCreditUseCase useCase;

    private static final ZoneId ZONE_ID =
            ZoneId.of("America/Sao_Paulo");

    private static final Clock FIXED_CLOCK =
            Clock.fixed(
                    Instant.parse("2026-08-24T15:00:00Z"),
                    ZONE_ID
            );

    @BeforeEach
    void setUp() {
        useCase = new ContractCreditUseCase(
                creditRepository,
                creditService,
                installmentFactory,
                calculationService,
                dueDatePolicy,
                eventPublisher,
                FIXED_CLOCK
        );
    }

    @Test
    @DisplayName("Should contract credit and publish event")
    void shouldContractCredit() {

        Credit credit =
                CreditTestFactory.approvedCredit();

        CreditId creditId =
                credit.getId();


        when(creditService.findCredit(creditId))
                .thenReturn(credit);


        CreditCalculationResult calculation =
                new CreditCalculationResult(
                        Money.zero(),
                        new Money(new BigDecimal("12000")),
                        TestConstants.FIVE_PERCENT
                );


        when(calculationService.calculate(
                any(),
                any(),
                any()
        )).thenReturn(calculation);


        List<Installment> installments =
                InstallmentTestFactory.installments(
                        creditId,
                        credit.getInstallmentsQuantity()
                );


        when(installmentFactory.createInstallments(
                any(),
                anyInt(),
                anyInt(),
                any(),
                any(),
                any()
        )).thenReturn(installments);


        useCase.execute(creditId);


        assertThat(credit.getStatus())
                .isEqualTo(
                        CreditStatus.CONTRACTED
                );


        assertThat(credit.getInstallments())
                .hasSize(
                        installments.size()
                );


        verify(creditRepository)
                .save(credit);


        verify(eventPublisher)
                .publishEvent(
                        any(CreditContractedEvent.class)
                );
    }

    @Test
    @DisplayName("Should calculate credit before creating installments")
    void shouldCalculateBeforeCreatingInstallments() {

        Credit credit =
                CreditTestFactory.approvedCredit();


        when(creditService.findCredit(
                credit.getId()
        )).thenReturn(credit);


        CreditCalculationResult calculation =
                new CreditCalculationResult(
                        Money.zero(),
                        new Money(new BigDecimal("12000")),
                        TestConstants.FIVE_PERCENT
                );


        when(calculationService.calculate(
                any(),
                any(),
                any()
        )).thenReturn(calculation);


        when(installmentFactory.createInstallments(
                any(),
                anyInt(),
                anyInt(),
                any(),
                any(),
                any()
        )).thenReturn(InstallmentTestFactory.installments(
                        credit.getId(),
                        credit.getInstallmentsQuantity()
                )
        );

        useCase.execute(
                credit.getId()
        );

        verify(calculationService)
                .calculate(
                        credit.getCreditType(),
                        credit.getRequestedAmount(),
                        credit.getInstallmentsQuantity()
                );

        LocalDate expectedDate = LocalDate.of(2026, 8, 24);
        verify(installmentFactory)
                .createInstallments(
                        credit.getId(),
                        1,
                        credit.getInstallmentsQuantity(),
                        calculation.totalAmount(),
                        expectedDate,
                        dueDatePolicy
                );
    }

    @Test
    @DisplayName("Should not save when contract fails")
    void shouldNotSaveWhenContractFails() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        when(creditService.findCredit(
                credit.getId()
        )).thenReturn(credit);


        assertThatThrownBy(() ->
                useCase.execute(
                        credit.getId()
                )
        );


        verify(creditRepository, never())
                .save(any());

        verify(eventPublisher, never())
                .publishEvent(any());
    }

}