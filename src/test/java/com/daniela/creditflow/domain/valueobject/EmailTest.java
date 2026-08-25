package com.daniela.creditflow.domain.valueobject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EmailTest {

    @Test
    @DisplayName("Should create valid email")
    void shouldCreateValidEmail() {

        Email email =
                new Email("test@email.com");

        assertThat(email.value())
                .isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("Should normalize email")
    void shouldNormalizeEmail() {

        Email email =
                new Email("  TEST@EMAIL.COM  ");

        assertThat(email.value())
                .isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("Should reject null email")
    void shouldRejectNullEmail() {

        assertThatThrownBy(() ->
                new Email(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Email cannot be null");
    }

    @Test
    @DisplayName("Should reject invalid email")
    void shouldRejectInvalidEmail() {

        assertThatThrownBy(() ->
                new Email("invalid-email")
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Invalid email");
    }

    @Test
    @DisplayName("Should accept email with valid special characters")
    void shouldAcceptEmailWithValidSpecialCharacters() {

        Email email =
                new Email("user.name+test@email.com");

        assertThat(email.value())
                .isEqualTo("user.name+test@email.com");
    }
}