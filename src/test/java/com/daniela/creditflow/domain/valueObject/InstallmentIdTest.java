package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class InstallmentIdTest {

    @Test
    @DisplayName("Should create InstallmentId with valid UUID")
    void shouldCreateInstallmentIdWithValidUuid() {

        UUID uuid = UUID.randomUUID();

        InstallmentId installmentId =
                new InstallmentId(uuid);

        assertThat(installmentId.value())
                .isEqualTo(uuid);
    }

    @Test
    @DisplayName("Should reject null UUID")
    void shouldRejectNullUuid() {

        assertThatThrownBy(() ->
                new InstallmentId(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("InstallmentId cannot be null");
    }

    @Test
    @DisplayName("Should generate UUID when created without value")
    void shouldGenerateUuidWhenCreatedWithoutValue() {

        InstallmentId installmentId =
                new InstallmentId();

        assertThat(installmentId.value())
                .isNotNull();
    }
}