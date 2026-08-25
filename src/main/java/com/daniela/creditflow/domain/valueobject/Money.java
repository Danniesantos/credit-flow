package com.daniela.creditflow.domain.valueobject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal value) {

    public Money {

        if (value == null) {
            throw new InvalidDomainStateException(
                    "Money cannot be null"
            );
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDomainStateException("Money cannot be negative");
        }
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    public Money add(Money other) {
        return new Money(value.add(other.value()));
    }

    public Money subtract(Money other) {
        return new Money(value.subtract(other.value()));
    }

    public Money multiply(BigDecimal factor) {
        return new Money(value.multiply(factor));
    }

    public boolean greaterThan(Money other) {
        return value.compareTo(other.value()) > 0;
    }

    public boolean lessThan(Money other) {
        return value.compareTo(other.value()) < 0;
    }

    public Money divide(Integer divisor) {
        if (divisor == null || divisor == 0) {
            throw new InvalidDomainStateException(
                    "Divisor cannot be zero"
            );
        }

        return new Money(
                value.divide(
                        BigDecimal.valueOf(divisor),
                        2,
                        RoundingMode.HALF_UP
                )
        );
    }
}
