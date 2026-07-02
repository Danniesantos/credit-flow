package com.daniela.creditflow.domain.credit.valueObject;

import com.daniela.creditflow.domain.exceptions.DomainException;

import java.util.Objects;

public record CreditScore(Integer value) {

    public CreditScore {
        Objects.requireNonNull(value, "Credit score cannot be null");
        if (value < 0 || value > 1000) {
            throw new DomainException(
                    "Credit score must be between 0 and 1000");
        }
    }

    public boolean isGood() {
        return value >= 700;
    }

    public boolean isExcellent() {
        return value >= 850;
    }

    public boolean isAverage() {
        return value >= 500;
    }

    public boolean isPoor() {
        return value < 500;
    }
}
