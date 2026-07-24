package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

public record PhoneNumber(String value) {

    public PhoneNumber {

        if (value == null) {
            throw new InvalidDomainStateException(
                    "Phone number cannot be null"
            );
        }

        value = normalize(value);

        validate(value);
    }

    private static String normalize(String value) {
        return value.replaceAll("\\D", "");
    }

    private static void validate(String value) {

        if (!value.matches("^\\d{10,11}$")) {
            throw new InvalidDomainStateException("Invalid phone number");
        }
    }
}
