package com.daniela.creditflow.application.installment.factory;

import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.application.installment.policy.MonthlyDueDatePolicy;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.Money;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.daniela.creditflow.support.TestConstants.TEST_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

class InstallmentFactoryTest {

    private InstallmentFactory factory;

    @BeforeEach
    void setUp() {
        factory = new InstallmentFactory();
    }

    @Test
    @DisplayName("Should create installments")
    void shouldCreateInstallments() {

        CreditId creditId = new CreditId();

        List<Installment> installments =
                factory.createInstallments(
                        creditId,
                        1,
                        3,
                        new Money(new BigDecimal("3000")),
                        TEST_DATE,
                        new MonthlyDueDatePolicy()
                );

        assertThat(installments)
                .hasSize(3);

        assertThat(installments)
                .extracting(Installment::getNumber)
                .containsExactly(1, 2, 3);

        assertThat(installments)
                .allMatch(Installment::isPending);
    }

    @Test
    @DisplayName("Should divide total amount between installments")
    void shouldDivideAmountBetweenInstallments() {

        List<Installment> installments =
                factory.createInstallments(
                        new CreditId(),
                        1,
                        3,
                        new Money(new BigDecimal("3000")),
                        TEST_DATE,
                        new MonthlyDueDatePolicy()
                );

        assertThat(installments)
                .extracting(i -> i.getAmount().value())
                .containsExactly(
                        new BigDecimal("1000.00"),
                        new BigDecimal("1000.00"),
                        new BigDecimal("1000.00")
                );
    }

    @Test
    @DisplayName("Should adjust last installment amount")
    void shouldAdjustLastInstallmentAmount() {

        List<Installment> installments =
                factory.createInstallments(
                        new CreditId(),
                        1,
                        3,
                        new Money(new BigDecimal("100")),
                        TEST_DATE,
                        new MonthlyDueDatePolicy()
                );

        assertThat(installments)
                .extracting(i -> i.getAmount().value())
                .containsExactly(
                        new BigDecimal("33.33"),
                        new BigDecimal("33.33"),
                        new BigDecimal("33.34")
                );
    }

    @Test
    @DisplayName("Should create installments starting from given number")
    void shouldCreateInstallmentsWithStartNumber() {

        List<Installment> installments =
                factory.createInstallments(
                        new CreditId(),
                        5,
                        3,
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        TEST_DATE,
                        new MonthlyDueDatePolicy()
                );

        assertThat(installments)
                .extracting(Installment::getNumber)
                .containsExactly(5, 6, 7);
    }

    @Test
    @DisplayName("Should not create installments with invalid quantity")
    void shouldNotCreateInstallmentsWithInvalidQuantity() {

        assertThatThrownBy(() ->
                factory.createInstallments(
                        new CreditId(),
                        1,
                        0,
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        TEST_DATE,
                        new MonthlyDueDatePolicy()
                ))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage(
                        "Installment quantity must be greater than zero"
                );
    }

    @Test
    @DisplayName("Should calculate due date using policy")
    void shouldCalculateDueDateUsingPolicy() {

        DueDatePolicy policy = mock(DueDatePolicy.class);

        when(policy.calculate(1, TEST_DATE))
                .thenReturn(TEST_DATE.plusMonths(1));

        factory.createInstallments(
                new CreditId(),
                1,
                1,
                TestConstants.TOTAL_CREDIT_AMOUNT,
                TEST_DATE,
                policy
        );

        verify(policy)
                .calculate(1, TEST_DATE);
    }
}

