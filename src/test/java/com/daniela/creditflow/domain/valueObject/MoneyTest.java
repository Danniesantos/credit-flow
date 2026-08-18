package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MoneyTest {

    @Test
    @DisplayName("Should create money with positive value")
    void shouldCreateMoneyWithPositiveValue() {

        Money money = new Money(new BigDecimal("100.00"));

        assertThat(money.value())
                .isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Should create money with zero value")
    void shouldCreateMoneyWithZeroValue() {

        Money money = Money.zero();

        assertThat(money.isZero())
                .isTrue();
    }

    @Test
    @DisplayName("Should not create money with null value")
    void shouldNotCreateMoneyWithNullValue() {

        assertThatThrownBy(() ->
                new Money(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Money cannot be null");
    }

    @Test
    @DisplayName("Should not create money with negative value")
    void shouldNotCreateMoneyWithNegativeValue() {

        assertThatThrownBy(() ->
                new Money(new BigDecimal("-10"))
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Money cannot be negative");
    }

    @Test
    @DisplayName("Should add money values")
    void shouldAddMoneyValues() {

        Money result = new Money(new BigDecimal("100"))
                .add(new Money(new BigDecimal("50")));

        assertThat(result.value())
                .isEqualByComparingTo("150");
    }

    @Test
    @DisplayName("Should multiply money value")
    void shouldMultiplyMoneyValue() {

        Money result = new Money(new BigDecimal("100"))
                .multiply(new BigDecimal("2.5"));

        assertThat(result.value())
                .isEqualByComparingTo("250");
    }

    @Test
    @DisplayName("Should divide money value")
    void shouldDivideMoneyValue() {

        Money result = new Money(new BigDecimal("100"))
                .divide(4);

        assertThat(result.value())
                .isEqualByComparingTo("25.00");
    }

    @Test
    @DisplayName("Should not divide money by zero")
    void shouldNotDivideMoneyByZero() {

        assertThatThrownBy(() ->
                new Money(new BigDecimal("100"))
                        .divide(0)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Divisor cannot be zero");
    }

    @Test
    @DisplayName("Should return true when money is greater than another value")
    void shouldIdentifyGreaterMoney() {

        Money money = new Money(new BigDecimal("100"));

        assertThat(
                money.greaterThan(
                        new Money(new BigDecimal("50"))
                )
        )
                .isTrue();
    }

    @Test
    @DisplayName("Should identify less money")
    void shouldIdentifyLessMoney() {

        Money money = new Money(new BigDecimal("50"));

        assertThat(
                money.lessThan(
                        new Money(new BigDecimal("100"))
                )
        )
                .isTrue();
    }

    @Test
    @DisplayName("Should round division result")
    void shouldRoundDivisionResult() {

        Money result = new Money(new BigDecimal("10"))
                .divide(3);

        assertThat(result.value())
                .isEqualByComparingTo("3.33");
    }

    @Test
    @DisplayName("Should subtract money values")
    void shouldSubtractMoneyValues() {

        Money result = new Money(new BigDecimal("100"))
                .subtract(new Money(new BigDecimal("30")));

        assertThat(result.value())
                .isEqualByComparingTo("70");
    }

    @Test
    @DisplayName("Should not subtract resulting in negative money")
    void shouldNotSubtractNegativeMoney() {

        assertThatThrownBy(() ->
                new Money(new BigDecimal("10"))
                        .subtract(new Money(new BigDecimal("20")))
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Money cannot be negative");
    }

    @Test
    @DisplayName("Should return false when money is not zero")
    void shouldReturnFalseWhenMoneyIsNotZero() {

        Money money = new Money(new BigDecimal("100"));

        assertThat(money.isZero())
                .isFalse();
    }

    @Test
    @DisplayName("Should keep original money after addition")
    void shouldKeepOriginalMoneyAfterAddition() {

        Money original = new Money(new BigDecimal("100"));

        Money result = original.add(
                new Money(new BigDecimal("50"))
        );

        assertThat(original.value())
                .isEqualByComparingTo("100");

        assertThat(result.value())
                .isEqualByComparingTo("150");
    }

    @Test
    @DisplayName("Should not divide money by null divisor")
    void shouldNotDivideMoneyByNullDivisor() {

        assertThatThrownBy(() ->
                new Money(new BigDecimal("100"))
                        .divide(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Divisor cannot be zero");
    }
}