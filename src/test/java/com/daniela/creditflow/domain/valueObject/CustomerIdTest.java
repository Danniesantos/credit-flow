package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerIdTest {

    @Test
    @DisplayName("Should create CustomerId with valid UUID")
    void shouldCreateCustomerIdWithValidUuid() {

        UUID uuid = UUID.randomUUID();

        CustomerId customerId =
                new CustomerId(uuid);

        assertThat(customerId.value())
                .isEqualTo(uuid);
    }

    @Test
    @DisplayName("Should reject null UUID")
    void shouldRejectNullUuid() {

        assertThatThrownBy(() ->
                new CustomerId(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("CustomerId cannot be null");
    }

    @Test
    @DisplayName("Should generate UUID when created without value")
    void shouldGenerateUuidWhenCreatedWithoutValue() {

        CustomerId customerId =
                new CustomerId();

        assertThat(customerId.value())
                .isNotNull();
    }
}