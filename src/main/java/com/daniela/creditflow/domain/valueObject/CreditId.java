package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

import java.util.UUID;

public record CreditId(UUID value) {

    public CreditId {
        if (value == null) {
            throw new InvalidDomainStateException(
                    "CPF cannot be null"
            );
        }
    }

    public CreditId() {
        this(UUID.randomUUID());
    }
}
