package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class InterestRateTest {

    @Test
    @DisplayName("Should create interest rate with valid value")
    void shouldCreateInterestRateWithValidValue() {

        InterestRate rate = new InterestRate(
                new BigDecimal("0.05")
        );

        assertThat(rate.value())
                .isEqualByComparingTo("0.05");
    }

    @Test
    @DisplayName("Should create interest rate with zero value")
    void shouldCreateInterestRateWithZeroValue() {

        InterestRate rate = new InterestRate(
                BigDecimal.ZERO
        );

        assertThat(rate.value())
                .isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("Should not create interest rate with null value")
    void shouldNotCreateInterestRateWithNullValue() {

        assertThatThrownBy(() ->
                new InterestRate(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("InterestRate cannot be null");
    }

    @Test
    @DisplayName("Should not create negative interest rate")
    void shouldNotCreateNegativeInterestRate() {

        assertThatThrownBy(() ->
                new InterestRate(new BigDecimal("-0.05"))
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Interest rate cannot be negative");
    }

    @Test
    @DisplayName("Should not create interest rate above 100 percent")
    void shouldNotCreateInterestRateAboveLimit() {

        assertThatThrownBy(() ->
                new InterestRate(new BigDecimal("1.01"))
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Interest rate cannot exceed 100%");
    }

    @Test
    @DisplayName("Should calculate interest amount")
    void shouldCalculateInterestAmount() {

        InterestRate rate = new InterestRate(
                new BigDecimal("0.05")
        );

        Money result = rate.calculateInterest(
                new Money(new BigDecimal("1000"))
        );

        assertThat(result.value())
                .isEqualByComparingTo("50.00");
    }

    @Test
    @DisplayName("Should round calculated interest")
    void shouldRoundCalculatedInterest() {

        InterestRate rate = new InterestRate(
                new BigDecimal("0.0333")
        );

        Money result = rate.calculateInterest(
                new Money(new BigDecimal("100"))
        );

        assertThat(result.value())
                .isEqualByComparingTo("3.33");
    }

    @Test
    @DisplayName("Should not calculate interest with null amount")
    void shouldNotCalculateInterestWithNullAmount() {

        InterestRate rate = new InterestRate(
                new BigDecimal("0.05")
        );

        assertThatThrownBy(() ->
                rate.calculateInterest(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Amount cannot be null");
    }

    @Test
    @DisplayName("Should return percentage value")
    void shouldReturnPercentageValue() {

        InterestRate rate = new InterestRate(
                new BigDecimal("0.05")
        );

        assertThat(rate.percentage())
                .isEqualByComparingTo("5.00");
    }

    @Test
    @DisplayName("Should calculate zero interest when rate is zero")
    void shouldCalculateZeroInterest() {

        InterestRate rate = new InterestRate(BigDecimal.ZERO);

        Money result = rate.calculateInterest(
                new Money(new BigDecimal("1000"))
        );

        assertThat(result.value())
                .isEqualByComparingTo("0.00");
    }

}