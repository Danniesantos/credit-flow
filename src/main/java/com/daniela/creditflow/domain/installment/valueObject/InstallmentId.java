package com.daniela.creditflow.domain.installment.valueObject;

import java.util.Objects;
import java.util.UUID;

public record InstallmentId(UUID value) {
    public InstallmentId {
        Objects.requireNonNull(value, "InstallmentId cannot be null");
    }

    public InstallmentId() {
        this(UUID.randomUUID());
    }
}
