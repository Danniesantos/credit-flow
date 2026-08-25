package com.daniela.creditflow.domain.valueobject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        if (value == null) {
            throw new InvalidDomainStateException(
                    "CustomerId cannot be null"
            );
        }
    }

    public CustomerId() {
        this(UUID.randomUUID());
    }
}
