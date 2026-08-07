package com.daniela.creditflow.application.credit.calculation;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.valueObject.Money;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CreditCalculationResultTest {

    @Test
    @DisplayName("Should create credit calculation result")
    void shouldCreateCreditCalculationResult() {

        CreditCalculationResult result =
                new CreditCalculationResult(
                        new Money(new BigDecimal("500")),
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        TestConstants.FIVE_PERCENT
                );

        assertThat(result.interestAmount())
                .isEqualTo(new Money(new BigDecimal("500")));

        assertThat(result.totalAmount())
                .isEqualTo(TestConstants.TOTAL_CREDIT_AMOUNT);

        assertThat(result.interestRate())
                .isEqualTo(TestConstants.FIVE_PERCENT);
    }


    @Test
    @DisplayName("Should calculate installment amount")
    void shouldCalculateInstallmentAmount() {

        CreditCalculationResult result =
                new CreditCalculationResult(
                        new Money(new BigDecimal("500")),
                        new Money(new BigDecimal("3000")),
                        TestConstants.FIVE_PERCENT
                );


        Money installment =
                result.installmentAmount(3);


        assertThat(installment.value())
                .isEqualByComparingTo("1000");
    }


    @Test
    @DisplayName("Should round installment amount")
    void shouldRoundInstallmentAmount() {

        CreditCalculationResult result =
                new CreditCalculationResult(
                        new Money(new BigDecimal("0")),
                        new Money(new BigDecimal("100")),
                        TestConstants.FIVE_PERCENT
                );


        Money installment =
                result.installmentAmount(3);


        assertThat(installment.value())
                .isEqualByComparingTo("33.33");
    }


    @Test
    @DisplayName("Should not calculate installment with zero installments")
    void shouldNotCalculateInstallmentWithZeroInstallments() {

        CreditCalculationResult result =
                new CreditCalculationResult(
                        Money.zero(),
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        TestConstants.FIVE_PERCENT
                );


        assertThatThrownBy(() ->
                result.installmentAmount(0)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Divisor cannot be zero");
    }
}