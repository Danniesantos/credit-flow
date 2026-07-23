package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

import java.util.UUID;

public record InstallmentId(UUID value) {

    public InstallmentId {
        if (value == null) {
            throw new InvalidDomainStateException(
                    "InstallmentId cannot be null"
            );
        }
    }

    public InstallmentId() {
        this(UUID.randomUUID());
    }
}
