package com.daniela.creditflow.domain.credit.valueObject;

import java.util.Objects;
import java.util.UUID;

public record CreditId(UUID value) {

    public CreditId {
        Objects.requireNonNull(value, "CreditId cannot be null");
    }

    public CreditId() {
        this(UUID.randomUUID());
    }
}
