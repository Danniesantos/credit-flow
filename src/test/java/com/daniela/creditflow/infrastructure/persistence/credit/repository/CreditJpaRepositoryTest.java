package com.daniela.creditflow.infrastructure.persistence.credit.repository;

import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.model.CustomerStatus;
import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import com.daniela.creditflow.infrastructure.persistence.installment.entity.InstallmentEntity;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class CreditJpaRepositoryTest {

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
    private CreditJpaRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should find credit with installments")
    void shouldFindCreditWithInstallments() {

        CustomerEntity customer =
                createCustomer();

        CreditEntity credit =
                createCredit(customer);

        InstallmentEntity firstInstallment =
                createInstallment(credit, 1);

        InstallmentEntity secondInstallment =
                createInstallment(credit, 2);

        entityManager.persist(customer);
        entityManager.persist(credit);
        entityManager.persist(firstInstallment);
        entityManager.persist(secondInstallment);

        entityManager.flush();
        entityManager.clear();

        Optional<CreditEntity> result =
                repository.findByIdWithInstallments(credit.getId());

        assertThat(result)
                .isPresent();

        assertThat(result.get().getInstallments())
                .hasSize(2);

        assertThat(result.get().getInstallments())
                .extracting(InstallmentEntity::getNumber)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    @DisplayName("Should return empty when credit does not exist")
    void shouldReturnEmptyWhenCreditDoesNotExist() {

        UUID creditId =
                UUID.randomUUID();

        Optional<CreditEntity> result =
                repository.findByIdWithInstallments(creditId);

        assertThat(result)
                .isEmpty();
    }

    @Test
    @DisplayName("Should find credits with overdue installments")
    void shouldFindCreditsWithOverdueInstallments() {

        CustomerEntity customer = createCustomer();

        CreditEntity credit = createCredit(customer);

        InstallmentEntity installment =
                createInstallment(
                        credit,
                        1,
                        InstallmentStatus.PENDING,
                        LocalDate.now().minusDays(10)
                );

        entityManager.persist(customer);
        entityManager.persist(credit);
        entityManager.persist(installment);

        entityManager.flush();
        entityManager.clear();

        Page<CreditEntity> result =
                repository.findCreditsWithOverdueInstallments(
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent())
                .hasSize(1);

        assertThat(result.getContent().get(0).getId())
                .isEqualTo(credit.getId());
    }

    @Test
    @DisplayName("Should not find credits with future installments")
    void shouldNotFindCreditsWithFutureInstallments() {

        CustomerEntity customer = createCustomer();

        CreditEntity credit = createCredit(customer);

        InstallmentEntity installment =
                createInstallment(
                        credit,
                        1,
                        InstallmentStatus.PENDING,
                        LocalDate.now().plusDays(10)
                );

        entityManager.persist(customer);
        entityManager.persist(credit);
        entityManager.persist(installment);

        entityManager.flush();
        entityManager.clear();

        Page<CreditEntity> result =
                repository.findCreditsWithOverdueInstallments(
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent())
                .isEmpty();
    }

    @Test
    @DisplayName("Should not find credits with paid overdue installments")
    void shouldNotFindCreditsWithPaidOverdueInstallments() {

        CustomerEntity customer = createCustomer();

        CreditEntity credit = createCredit(customer);

        InstallmentEntity installment =
                createInstallment(
                        credit,
                        1,
                        InstallmentStatus.PAID,
                        LocalDate.now().minusDays(10)
                );

        entityManager.persist(customer);
        entityManager.persist(credit);
        entityManager.persist(installment);

        entityManager.flush();
        entityManager.clear();

        Page<CreditEntity> result =
                repository.findCreditsWithOverdueInstallments(
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent())
                .isEmpty();
    }

    @Test
    @DisplayName("Should return true when customer has open credit")
    void shouldReturnTrueWhenCustomerHasOpenCredit() {

        CustomerEntity customer = createCustomer();

        CreditEntity credit =
                createCredit(customer, CreditStatus.APPROVED);

        entityManager.persist(customer);
        entityManager.persist(credit);

        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByCustomerIdAndStatusIn(
                        customer.getId(),
                        CreditStatus.openStatuses()
                );

        assertThat(result)
                .isTrue();
    }

    @Test
    @DisplayName("Should return false when customer has no open credit")
    void shouldReturnFalseWhenCustomerHasNoOpenCredit() {

        CustomerEntity customer = createCustomer();

        CreditEntity credit =
                createCredit(customer, CreditStatus.REJECTED);

        entityManager.persist(customer);
        entityManager.persist(credit);

        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByCustomerIdAndStatusIn(
                        customer.getId(),
                        CreditStatus.openStatuses()
                );

        assertThat(result)
                .isFalse();
    }

    @Test
    @DisplayName("Should return false when customer has no credits")
    void shouldReturnFalseWhenCustomerHasNoCredits() {

        CustomerEntity customer =
                createCustomer();

        CustomerEntity anotherCustomer =
                createCustomer(
                        UUID.randomUUID(),
                        "12345678909",
                        "another@email.com"
                );

        CreditEntity credit =
                createCredit(customer, CreditStatus.APPROVED);

        entityManager.persist(customer);
        entityManager.persist(anotherCustomer);
        entityManager.persist(credit);

        entityManager.flush();
        entityManager.clear();

        boolean result =
                repository.existsByCustomerIdAndStatusIn(
                        anotherCustomer.getId(),
                        CreditStatus.openStatuses()
                );

        assertThat(result)
                .isFalse();
    }

    private CustomerEntity createCustomer() {

        Instant now =
                Instant.now();

        return new CustomerEntity(
                UUID.randomUUID(),
                "Testando",
                "29246272064",
                "testando@email.com",
                LocalDate.of(1992, 1, 10),
                "19999999999",
                BigDecimal.valueOf(5000),
                800,
                CustomerStatus.ACTIVE,
                now,
                now
        );
    }

    private CustomerEntity createCustomer(
            UUID id,
            String cpf,
            String email
    ) {
        return new CustomerEntity(
                id,
                "Testando",
                cpf,
                email,
                TestConstants.CUSTOMER_BIRTH_DATE,
                "19999999999",
                TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                800,
                CustomerStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
    }

    private CreditEntity createCredit(
            CustomerEntity customer) {

        Instant now =
                Instant.now();

        return new CreditEntity(
                UUID.randomUUID(),
                customer,
                BigDecimal.valueOf(10_000),
                CreditType.PERSONAL,
                BigDecimal.valueOf(0.05),
                2,
                CreditStatus.UNDER_ANALYSIS,
                now,
                now
        );
    }

    private InstallmentEntity createInstallment(
            CreditEntity credit,
            int number,
            InstallmentStatus status,
            LocalDate dueDate) {

        return new InstallmentEntity(
                UUID.randomUUID(),
                number,
                BigDecimal.valueOf(1000),
                dueDate,
                null,
                status,
                status == InstallmentStatus.PAID
                        ? Instant.now()
                        : null,
                credit
        );
    }

    private InstallmentEntity createInstallment(
            CreditEntity credit,
            int number) {

        return createInstallment(
                credit,
                number,
                InstallmentStatus.PENDING,
                LocalDate.now().plusDays(number)
        );
    }

    private CreditEntity createCredit(
            CustomerEntity customer,
            CreditStatus status) {

        Instant now = Instant.now();

        return new CreditEntity(
                UUID.randomUUID(),
                customer,
                BigDecimal.valueOf(10_000),
                CreditType.PERSONAL,
                BigDecimal.valueOf(0.05),
                2,
                status,
                now,
                now
        );
    }
}