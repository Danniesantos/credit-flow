package com.daniela.creditflow.domain.valueobject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CreditIdTest {

    @Test
    @DisplayName("Should create CreditId with valid UUID")
    void shouldCreateCreditIdWithValidUuid() {

        UUID uuid = UUID.randomUUID();

        CreditId creditId =
                new CreditId(uuid);

        assertThat(creditId.value())
                .isEqualTo(uuid);
    }

    @Test
    @DisplayName("Should reject null UUID")
    void shouldRejectNullUuid() {

        assertThatThrownBy(() ->
                new CreditId(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Credit ID cannot be null");
    }

    @Test
    @DisplayName("Should generate UUID when created without value")
    void shouldGenerateUuidWhenCreatedWithoutValue() {

        CreditId creditId =
                new CreditId();

        assertThat(creditId.value())
                .isNotNull();
    }
}