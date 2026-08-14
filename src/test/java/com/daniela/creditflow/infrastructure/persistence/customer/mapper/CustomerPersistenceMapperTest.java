package com.daniela.creditflow.infrastructure.persistence.customer.mapper;

import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerStatus;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import com.daniela.creditflow.support.CustomerTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerPersistenceMapperTest {

    private final CustomerPersistenceMapper mapper =
            new CustomerPersistenceMapper();

    @Test
    @DisplayName("Should map customer to entity")
    void shouldMapCustomerToEntity() {

        Customer customer =
                CustomerTestFactory.customer();

        CustomerEntity result =
                mapper.toEntity(customer);

        assertThat(result.getId())
                .isEqualTo(customer.getId().value());

        assertThat(result.getName())
                .isEqualTo(customer.getName());

        assertThat(result.getCpf())
                .isEqualTo(customer.getCpf().value());

        assertThat(result.getEmail())
                .isEqualTo(customer.getEmail().value());

        assertThat(result.getDateOfBirth())
                .isEqualTo(customer.getDateOfBirth());

        assertThat(result.getPhoneNumber())
                .isEqualTo(customer.getPhoneNumber().value());

        assertThat(result.getMonthlyIncome())
                .isEqualByComparingTo(
                        customer.getMonthlyIncome().value()
                );

        assertThat(result.getCreditScore())
                .isEqualTo(customer.getCreditScore().value());

        assertThat(result.getStatus())
                .isEqualTo(customer.getStatus());

        assertThat(result.getCreatedAt())
                .isEqualTo(customer.getCreatedAt());

        assertThat(result.getUpdatedAt())
                .isEqualTo(customer.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map customer entity to domain")
    void shouldMapCustomerEntityToDomain() {

        UUID customerId = UUID.randomUUID();

        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        CustomerEntity entity =
                new CustomerEntity(
                        customerId,
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800,
                        CustomerStatus.ACTIVE,
                        createdAt,
                        updatedAt
                );

        Customer result =
                mapper.toDomain(entity);

        assertThat(result.getId().value())
                .isEqualTo(customerId);

        assertThat(result.getName())
                .isEqualTo(entity.getName());

        assertThat(result.getCpf().value())
                .isEqualTo(entity.getCpf());

        assertThat(result.getEmail().value())
                .isEqualTo(entity.getEmail());

        assertThat(result.getDateOfBirth())
                .isEqualTo(entity.getDateOfBirth());

        assertThat(result.getPhoneNumber().value())
                .isEqualTo(entity.getPhoneNumber());

        assertThat(result.getMonthlyIncome().value())
                .isEqualByComparingTo(entity.getMonthlyIncome());

        assertThat(result.getCreditScore().value())
                .isEqualTo(entity.getCreditScore());

        assertThat(result.getStatus())
                .isEqualTo(entity.getStatus());

        assertThat(result.getCreatedAt())
                .isEqualTo(entity.getCreatedAt());

        assertThat(result.getUpdatedAt())
                .isEqualTo(entity.getUpdatedAt());
    }
}