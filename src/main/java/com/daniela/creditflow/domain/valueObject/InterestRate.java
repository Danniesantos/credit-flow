package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record InterestRate(BigDecimal value) {

    public InterestRate {

        if (value == null) {
            throw new InvalidDomainStateException(
                    "InterestRate cannot be null"
            );
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDomainStateException(
                    "Interest rate cannot be negative");
        }
        if (value.compareTo(BigDecimal.ONE) > 0) {
            throw new InvalidDomainStateException("Interest rate cannot exceed 100%");
        }
    }

    public Money calculateInterest(
            Money amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");

        return new Money(
                amount.value()
                        .multiply(value)
                        .setScale(2, RoundingMode.HALF_UP));
    }

    public BigDecimal percentage() {
        return value
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
