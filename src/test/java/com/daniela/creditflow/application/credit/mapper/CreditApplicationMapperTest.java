package com.daniela.creditflow.application.credit.mapper;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.dto.output.*;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.application.installment.dto.output.OverdueInstallmentOutput;
import com.daniela.creditflow.application.installment.mapper.InstallmentOutputMapper;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditApplicationMapperTest {

    @Mock
    private InstallmentOutputMapper installmentMapper;

    @InjectMocks
    private CreditApplicationMapper mapper;

    @Test
    @DisplayName("Should map simulation output")
    void shouldMapSimulationOutput() {

        CreditCalculationResult calculation =
                new CreditCalculationResult(
                        new Money(new BigDecimal("500")),
                        new Money(new BigDecimal("10500")),
                        TestConstants.FIVE_PERCENT
                );

        SimulateCreditOutput output =
                mapper.toSimulateOutput(
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        calculation,
                        12,
                        new Money(new BigDecimal("875"))
                );

        assertThat(output.requestedAmount())
                .isEqualByComparingTo("10000");

        assertThat(output.interestRate())
                .isEqualByComparingTo("5.00");

        assertThat(output.totalAmount())
                .isEqualByComparingTo("10500");

        assertThat(output.installments())
                .isEqualTo(12);

        assertThat(output.installmentAmount())
                .isEqualByComparingTo("875");
    }

    @Test
    @DisplayName("Should map credit output")
    void shouldMapCreditOutput() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        RequestCreditOutput output =
                mapper.toCreditOutput(credit);

        assertThat(output.creditId())
                .isEqualTo(credit.getId().value());

        assertThat(output.customerId())
                .isEqualTo(credit.getCustomerId().value());

        assertThat(output.requestedAmount())
                .isEqualByComparingTo(
                        credit.getRequestedAmount().value()
                );

        assertThat(output.installments())
                .isEqualTo(
                        credit.getInstallmentsQuantity()
                );

        assertThat(output.interestRate())
                .isEqualByComparingTo("5.00");

        assertThat(output.creditType())
                .isEqualTo(credit.getCreditType());

        assertThat(output.status())
                .isEqualTo(credit.getStatus());
    }

    @Test
    @DisplayName("Should map credit details output")
    void shouldMapCreditDetailsOutput() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        when(installmentMapper.toOutput(any()))
                .thenReturn(
                        new InstallmentDetailsOutput(
                                UUID.randomUUID(),
                                1,
                                BigDecimal.valueOf(1000),
                                LocalDate.now(),
                                PaymentMethod.PIX,
                                InstallmentStatus.PENDING,
                                null
                        )
                );

        CreditDetailsOutput output =
                mapper.toDetailsOutput(credit);

        assertThat(output.creditId())
                .isEqualTo(credit.getId().value());

        assertThat(output.installments())
                .hasSize(
                        credit.getInstallments().size()
                );

        verify(installmentMapper,
                times(credit.getInstallments().size()))
                .toOutput(any());
    }

    @Test
    @DisplayName("Should map balance output")
    void shouldMapBalanceOutput() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        BalanceOutput output =
                mapper.toBalanceOutput(credit);

        assertThat(output.totalContractAmount())
                .isEqualByComparingTo(
                        credit.totalInstallmentsAmount().value()
                );

        assertThat(output.paidAmount())
                .isEqualByComparingTo(
                        credit.totalPaidAmount().value()
                );

        assertThat(output.remainingAmount())
                .isEqualByComparingTo(
                        credit.remainingAmount().value()
                );

        assertThat(output.remainingInstallments())
                .isEqualTo(
                        credit.remainingInstallments()
                );
    }

    @Test
    @DisplayName("Should map overdue output")
    void shouldMapOverdueOutput() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        when(installmentMapper.toOverdueOutput(any()))
                .thenReturn(
                        new OverdueInstallmentOutput(
                                UUID.randomUUID(),
                                1,
                                BigDecimal.valueOf(1000),
                                LocalDate.now().minusDays(10),
                                10L
                        )
                );

        OverdueOutput output =
                mapper.toOverdueOutput(credit);

        assertThat(output.hasOverdueInstallments())
                .isTrue();

        assertThat(output.overdueInstallmentsQuantity())
                .isEqualTo(
                        Long.valueOf(
                                credit.overdueInstallmentsQuantity()
                        )
                );

        assertThat(output.overdueInstallmentsQuantity())
                .isEqualTo(
                        Long.valueOf(
                                credit.overdueInstallmentsQuantity()
                        )
                );

        assertThat(output.overdueAmount())
                .isEqualByComparingTo(
                        credit.overdueAmount().value()
                );

        verify(installmentMapper,
                times(credit.overdueInstallments().size()))
                .toOverdueOutput(any());
    }

    @Test
    @DisplayName("Should map debtor output")
    void shouldMapDebtorOutput() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        DebtorOutput output =
                mapper.toDebtorOutput(credit);

        assertThat(output.creditId())
                .isEqualTo(
                        credit.getId().value()
                );

        assertThat(output.customerId())
                .isEqualTo(
                        credit.getCustomerId().value()
                );

        assertThat(output.overdueInstallments())
                .isEqualTo(
                        credit.overdueInstallmentsQuantity()
                );

        assertThat(output.overdueAmount())
                .isEqualByComparingTo(
                        credit.overdueAmount().value()
                );
    }
}