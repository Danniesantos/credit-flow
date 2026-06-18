package com.daniela.creditflow.domain.customer.valueObject;

import com.daniela.creditflow.domain.exceptions.DomainException;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$");

    public Email {
        Objects.requireNonNull(value, "Email cannot be null");

        value = normalize(value);

        validate(value);

    }

    private static String normalize(String value) {
        return value.trim().toLowerCase();
    }

    private static void validate(String value) {

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid email");
        }
    }
}
