package com.daniela.creditflow.application.credit.calculation.strategy;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BusinessCreditStrategyTest {
    private BusinessCreditStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new BusinessCreditStrategy();
    }

    @Test
    @DisplayName("Should calculate business credit using 3 percent interest rate")
    void shouldCalculateBusinessCreditUsingThreePercentRate() {

        CreditCalculationResult result =
                strategy.calculate(
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        12
                );

        assertThat(result.interestRate().value())
                .isEqualByComparingTo("0.03");

        assertThat(result.interestAmount().value())
                .isEqualByComparingTo("300.00");

        assertThat(result.totalAmount().value())
                .isEqualByComparingTo("10300.00");
    }
}