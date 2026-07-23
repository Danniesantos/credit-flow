package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

public record CreditScore(Integer value) {

    public CreditScore {

        if (value == null) {
            throw new InvalidDomainStateException(
                    "Credit score cannot be null"
            );
        }
        if (value < 0 || value > 1000) {
            throw new InvalidDomainStateException(
                    "Credit score must be between 0 and 1000");
        }
    }

    public boolean isExcellent() {
        return value >= 850;
    }

    public boolean isGood() {
        return value >= 700 && value < 850;
    }

    public boolean isAverage() {
        return value >= 500 && value < 700;
    }

    public boolean isPoor() {
        return value < 500;
    }
}
