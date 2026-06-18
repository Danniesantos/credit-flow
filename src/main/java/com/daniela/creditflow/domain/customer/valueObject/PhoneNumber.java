package com.daniela.creditflow.domain.customer.valueObject;

import com.daniela.creditflow.domain.exceptions.DomainException;

import java.util.Objects;

public record PhoneNumber(String value) {

    public PhoneNumber {
        Objects.requireNonNull(value, "Phone number cannot be null");

        value = normalize(value);

        validate(value);

    }

    private static String normalize(String value) {
        return value.replaceAll("\\D", "");
    }

    private static void validate(String value) {

        if (!value.matches("^\\d{10,11}$")) {
            throw new DomainException("Invalid phone number");
        }
    }
}
