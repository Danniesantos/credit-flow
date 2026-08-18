package com.daniela.creditflow.application.credit.calculation.strategy;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PersonalCreditStrategyTest {

    private PersonalCreditStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PersonalCreditStrategy();
    }

    @Test
    @DisplayName("Should calculate personal credit using 5 percent interest rate")
    void shouldCalculatePersonalCreditUsingFivePercentInterestRate() {

        CreditCalculationResult result =
                strategy.calculate(
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        12
                );

        assertThat(result.interestRate().value())
                .isEqualByComparingTo("0.05");

        assertThat(result.interestAmount().value())
                .isEqualByComparingTo("500.00");

        assertThat(result.totalAmount().value())
                .isEqualByComparingTo("10500.00");
    }
}