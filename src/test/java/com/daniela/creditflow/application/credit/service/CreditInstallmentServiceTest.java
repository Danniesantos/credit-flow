package com.daniela.creditflow.application.credit.service;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.installment.factory.InstallmentFactory;
import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.valueObject.Money;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditInstallmentServiceTest {

    @Mock
    private CreditCalculationService calculationService;
    @Mock
    private InstallmentFactory installmentFactory;
    @Mock
    private DueDatePolicy dueDatePolicy;

    @InjectMocks
    private CreditInstallmentService service;

    @Test
    @DisplayName("Should generate installments")
    void shouldGenerateInstallments() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        List<Installment> expected =
                List.of(mock(Installment.class));

        CreditCalculationResult calculation =
                new CreditCalculationResult(
                        new Money(new BigDecimal("100")),
                        new Money(new BigDecimal("2100")),
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
        )).thenReturn(expected);

        List<Installment> result =
                service.generate(
                        credit,
                        2
                );

        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Should calculate remaining amount")
    void shouldCalculateRemainingAmount() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        when(calculationService.calculate(any(), any(), any()))
                .thenReturn(
                        new CreditCalculationResult(
                                Money.zero(),
                                TestConstants.TOTAL_CREDIT_AMOUNT,
                                TestConstants.FIVE_PERCENT
                        )
                );

        when(installmentFactory.createInstallments(
                any(),
                anyInt(),
                anyInt(),
                any(),
                any(),
                any()
        )).thenReturn(List.of());

        service.generate(credit, 3);

        verify(calculationService)
                .calculate(
                        eq(credit.getCreditType()),
                        eq(credit.remainingAmount()),
                        eq(3)
                );
    }

    @Test
    @DisplayName("Should create installments using calculated total amount")
    void shouldCreateInstallmentsUsingCalculatedTotalAmount() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        CreditCalculationResult calculation =
                new CreditCalculationResult(
                        Money.zero(),
                        new Money(new BigDecimal("2100")),
                        TestConstants.FIVE_PERCENT
                );

        when(calculationService.calculate(any(), any(), any()))
                .thenReturn(calculation);

        when(installmentFactory.createInstallments(
                any(),
                anyInt(),
                anyInt(),
                any(),
                any(),
                any()
        )).thenReturn(List.of());

        service.generate(credit, 2);

        verify(installmentFactory)
                .createInstallments(
                        eq(credit.getId()),
                        eq(credit.nextInstallmentNumber()),
                        eq(2),
                        eq(calculation.totalAmount()),
                        any(LocalDate.class),
                        eq(dueDatePolicy)
                );
    }
}