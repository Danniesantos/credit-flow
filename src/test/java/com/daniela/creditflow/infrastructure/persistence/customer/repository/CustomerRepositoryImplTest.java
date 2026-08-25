package com.daniela.creditflow.infrastructure.persistence.customer.repository;

import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerStatus;
import com.daniela.creditflow.domain.valueobject.CPF;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.domain.valueobject.Email;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import com.daniela.creditflow.infrastructure.persistence.customer.mapper.CustomerPersistenceMapper;
import com.daniela.creditflow.support.CustomerTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryImplTest {

    @Mock
    private CustomerJpaRepository jpaRepository;

    @Mock
    private CustomerPersistenceMapper mapper;

    @InjectMocks
    private CustomerRepositoryImpl repository;

    @Test
    @DisplayName("Should save customer successfully")
    void shouldSaveCustomerSuccessfully() {

        Customer customer =
                CustomerTestFactory.customer();

        CustomerEntity entity =
                customerEntity();

        CustomerEntity savedEntity =
                customerEntity();

        Customer savedCustomer =
                CustomerTestFactory.customer();

        when(mapper.toEntity(customer))
                .thenReturn(entity);

        when(jpaRepository.save(entity))
                .thenReturn(savedEntity);

        when(mapper.toDomain(savedEntity))
                .thenReturn(savedCustomer);

        Customer result =
                repository.save(customer);

        assertThat(result)
                .isEqualTo(savedCustomer);

        verify(mapper)
                .toEntity(customer);

        verify(jpaRepository)
                .save(entity);

        verify(mapper)
                .toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should find all customers")
    void shouldFindAllCustomers() {

        Pageable pageable =
                PageRequest.of(0, 10);

        CustomerEntity firstEntity =
                customerEntity();

        CustomerEntity secondEntity =
                customerEntity();

        Customer first =
                CustomerTestFactory.customer();

        Customer second =
                CustomerTestFactory.customer();

        Page<CustomerEntity> entityPage =
                new PageImpl<>(
                        List.of(firstEntity, secondEntity),
                        pageable,
                        2
                );

        when(jpaRepository.findAll(pageable))
                .thenReturn(entityPage);

        when(mapper.toDomain(firstEntity))
                .thenReturn(first);

        when(mapper.toDomain(secondEntity))
                .thenReturn(second);

        Page<Customer> result =
                repository.findAll(pageable);

        assertThat(result.getContent())
                .containsExactly(first, second);

        verify(jpaRepository)
                .findAll(pageable);

        verify(mapper)
                .toDomain(firstEntity);

        verify(mapper)
                .toDomain(secondEntity);
    }

    @Test
    @DisplayName("Should return empty page when there are no customers")
    void shouldReturnEmptyPageWhenThereAreNoCustomers() {

        Pageable pageable =
                PageRequest.of(0, 10);

        when(jpaRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));

        Page<Customer> result =
                repository.findAll(pageable);

        assertThat(result)
                .isEmpty();

        verify(jpaRepository)
                .findAll(pageable);

        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should find customer by id")
    void shouldFindCustomerById() {

        Customer customer =
                CustomerTestFactory.customer();

        CustomerId customerId =
                customer.getId();

        CustomerEntity entity =
                customerEntity();

        when(jpaRepository.findById(customerId.value()))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity))
                .thenReturn(customer);

        Optional<Customer> result =
                repository.findById(customerId);

        assertThat(result)
                .isPresent()
                .contains(customer);

        verify(jpaRepository)
                .findById(customerId.value());

        verify(mapper)
                .toDomain(entity);
    }

    @Test
    @DisplayName("Should return empty when customer is not found")
    void shouldReturnEmptyWhenCustomerIsNotFound() {

        CustomerId customerId =
                new CustomerId(UUID.randomUUID());

        when(jpaRepository.findById(customerId.value()))
                .thenReturn(Optional.empty());

        Optional<Customer> result =
                repository.findById(customerId);

        assertThat(result)
                .isEmpty();

        verify(jpaRepository)
                .findById(customerId.value());

        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should check if CPF exists")
    void shouldCheckIfCpfExists() {

        CPF cpf =
                new CPF("292.462.720-64");

        when(jpaRepository.existsByCpf(cpf.value()))
                .thenReturn(true);

        boolean result =
                repository.existsByCpf(cpf);

        assertThat(result)
                .isTrue();

        verify(jpaRepository)
                .existsByCpf(cpf.value());
    }

    @Test
    @DisplayName("Should check if email exists")
    void shouldCheckIfEmailExists() {

        Email email =
                new Email("testando@email.com");

        when(jpaRepository.existsByEmail(email.value()))
                .thenReturn(true);

        boolean result =
                repository.existsByEmail(email);

        assertThat(result)
                .isTrue();

        verify(jpaRepository)
                .existsByEmail(email.value());
    }

    @Test
    @DisplayName("Should check if another customer has the CPF")
    void shouldCheckIfAnotherCustomerHasCpf() {

        CPF cpf =
                new CPF("292.462.720-64");

        CustomerId customerId =
                new CustomerId(UUID.randomUUID());

        when(jpaRepository.existsByCpfAndIdNot(
                cpf.value(),
                customerId.value()
        )).thenReturn(true);

        boolean result =
                repository.existsByCpfAndIdNot(
                        cpf,
                        customerId
                );

        assertThat(result)
                .isTrue();

        verify(jpaRepository)
                .existsByCpfAndIdNot(
                        cpf.value(),
                        customerId.value()
                );
    }

    @Test
    @DisplayName("Should check if another customer has the email")
    void shouldCheckIfAnotherCustomerHasEmail() {

        Email email =
                new Email("testando@email.com");

        CustomerId customerId =
                new CustomerId(UUID.randomUUID());

        when(jpaRepository.existsByEmailAndIdNot(
                email.value(),
                customerId.value()
        )).thenReturn(true);

        boolean result =
                repository.existsByEmailAndIdNot(
                        email,
                        customerId
                );

        assertThat(result)
                .isTrue();

        verify(jpaRepository)
                .existsByEmailAndIdNot(
                        email.value(),
                        customerId.value()
                );
    }

    private CustomerEntity customerEntity() {

        return new CustomerEntity(
                UUID.randomUUID(),
                TestConstants.CUSTOMER_NAME,
                "29246272064",
                "testando@email.com",
                TestConstants.CUSTOMER_BIRTH_DATE,
                "19999999999",
                TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                800,
                CustomerStatus.ACTIVE,
                TestConstants.CREATED_AT,
                TestConstants.CREATED_AT
        );
    }
}