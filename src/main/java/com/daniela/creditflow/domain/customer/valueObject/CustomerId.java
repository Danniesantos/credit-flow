package com.daniela.creditflow.domain.customer.valueObject;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {
    public CustomerId {
        Objects.requireNonNull(value,"CustomerId cannot be null");
    }

    public CustomerId() {
        this(UUID.randomUUID());
    }
}
