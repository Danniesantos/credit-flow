package com.daniela.creditflow.infrastructure.persistence.customer.repository;

import com.daniela.creditflow.domain.model.CustomerStatus;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
class CustomerJpaRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(
            DynamicPropertyRegistry registry) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }

    @Autowired
    private CustomerJpaRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should return true when CPF exists")
    void shouldReturnTrueWhenCpfExists() {

        CustomerEntity customer = createCustomer(UUID.randomUUID(),
                "29246272064",
                "testando@email.com");

        entityManager.persist(customer);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByCpf(customer.getCpf());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when no other customer has the CPF")
    void shouldReturnFalseWhenNoOtherCustomerHasCpf() {

        CustomerEntity customer =
                createCustomer(
                        UUID.randomUUID(),
                        "29246272064",
                        "testando@email.com"
                );

        entityManager.persist(customer);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByCpfAndIdNot(
                        customer.getCpf(),
                        customer.getId()
                );

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when another customer has the email")
    void shouldReturnTrueWhenAnotherCustomerHasEmail() {

        CustomerEntity first =
                createCustomer(
                        UUID.randomUUID(),
                        "29246272064",
                        "first@email.com"
                );

        CustomerEntity second =
                createCustomer(
                        UUID.randomUUID(),
                        "12345678909",
                        "second@email.com"
                );

        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByEmailAndIdNot(
                        first.getEmail(),
                        second.getId()
                );

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when no other customer has the email")
    void shouldReturnFalseWhenNoOtherCustomerHasEmail() {

        CustomerEntity customer =
                createCustomer(
                        UUID.randomUUID(),
                        "29246272064",
                        "testando@email.com"
                );

        entityManager.persist(customer);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByEmailAndIdNot(
                        customer.getEmail(),
                        customer.getId()
                );

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when another customer has the CPF")
    void shouldReturnTrueWhenAnotherCustomerHasCpf() {

        CustomerEntity first =
                createCustomer(
                        UUID.randomUUID(),
                        "29246272064",
                        "first@email.com"
                );

        CustomerEntity second =
                createCustomer(
                        UUID.randomUUID(),
                        "12345678909",
                        "second@email.com"
                );

        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByCpfAndIdNot(
                        first.getCpf(),
                        second.getId()
                );

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when CPF does not exist")
    void shouldReturnFalseWhenCpfDoesNotExist() {

        CustomerEntity customer =
                createCustomer(
                        UUID.randomUUID(),
                        "29246272064",
                        "testando@email.com"
                );

        entityManager.persist(customer);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByCpf("99999999999");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {

        CustomerEntity customer =
                createCustomer(
                        UUID.randomUUID(),
                        "29246272064",
                        "testando@email.com"
                );

        entityManager.persist(customer);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByEmail(customer.getEmail());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {

        CustomerEntity customer =
                createCustomer(
                        UUID.randomUUID(),
                        "29246272064",
                        "testando@email.com"
                );

        entityManager.persist(customer);
        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByEmail("other@email.com");

        assertThat(result).isFalse();
    }

    private CustomerEntity createCustomer(
            UUID id,
            String cpf,
            String email) {

        return new CustomerEntity(
                id,
                "Testando",
                cpf,
                email,
                LocalDate.of(1992, 1, 10),
                "19999999999",
                BigDecimal.valueOf(5000),
                800,
                CustomerStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
    }

}