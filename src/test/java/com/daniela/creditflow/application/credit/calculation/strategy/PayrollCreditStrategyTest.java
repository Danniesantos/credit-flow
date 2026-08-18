package com.daniela.creditflow.application.credit.calculation.strategy;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PayrollCreditStrategyTest {

    private PayrollCreditStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PayrollCreditStrategy();
    }

    @Test
    @DisplayName("Should calculate payroll credit using 1.5 percent interest rate")
    void shouldCalculatePayrollCreditUsingOnePointFivePercentInterestRate() {

        CreditCalculationResult result =
                strategy.calculate(
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        12
                );

        assertThat(result.interestRate().value())
                .isEqualByComparingTo("0.015");

        assertThat(result.interestAmount().value())
                .isEqualByComparingTo("150.00");

        assertThat(result.totalAmount().value())
                .isEqualByComparingTo("10150.00");
    }
}