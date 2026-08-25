package com.daniela.creditflow.application.credit.calculation.strategy;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.domain.valueobject.InterestRate;
import com.daniela.creditflow.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BaseCreditStrategyTest {

    @Test
    @DisplayName("Should calculate credit using strategy rate")
    void shouldCalculateCreditUsingStrategyRate() {

        InterestRate rate =
                new InterestRate(BigDecimal.valueOf(0.05));

        BaseCreditStrategy strategy =
                new BaseCreditStrategy() {
                    @Override
                    protected InterestRate rate() {
                        return rate;
                    }
                };


        CreditCalculationResult result =
                strategy.calculate(
                        new Money(new BigDecimal("1000")),
                        12
                );


        assertThat(result.interestAmount().value())
                .isEqualByComparingTo("50.00");

        assertThat(result.totalAmount().value())
                .isEqualByComparingTo("1050.00");

        assertThat(result.interestRate())
                .isEqualTo(rate);
    }
}