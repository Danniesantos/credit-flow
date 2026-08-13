package com.daniela.creditflow.application.customer.service;

import com.daniela.creditflow.domain.exceptions.CpfAlreadyExistsException;
import com.daniela.creditflow.domain.exceptions.CustomerHasOpenCreditsException;
import com.daniela.creditflow.domain.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.exceptions.EmailAlreadyExistsException;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import com.daniela.creditflow.domain.valueObject.CPF;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import com.daniela.creditflow.domain.valueObject.Email;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CustomerService service;

    @Test
    @DisplayName("Should throw exception when CPF already exists")
    void shouldThrowExceptionWhenCpfAlreadyExists() {

        CPF cpf = new CPF("292.462.720-64");
        Email email = new Email("testando@email.com");

        when(customerRepository.existsByCpf(cpf))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.validateForCreate(cpf, email)
        )
                .isInstanceOf(CpfAlreadyExistsException.class);

        verify(customerRepository)
                .existsByCpf(cpf);

        verify(customerRepository, never())
                .existsByEmail(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        CPF cpf = new CPF("292.462.720-64");
        Email email = new Email("testando@email.com");

        when(customerRepository.existsByCpf(cpf))
                .thenReturn(false);

        when(customerRepository.existsByEmail(email))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.validateForCreate(cpf, email)
        )
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(customerRepository)
                .existsByEmail(email);
    }

    @Test
    @DisplayName("Should allow creation when CPF and email are available")
    void shouldAllowCreationWhenCpfAndEmailAreAvailable() {

        CPF cpf = new CPF("292.462.720-64");
        Email email = new Email("testando@email.com");

        when(customerRepository.existsByCpf(cpf))
                .thenReturn(false);

        when(customerRepository.existsByEmail(email))
                .thenReturn(false);

        assertThatCode(() ->
                service.validateForCreate(cpf, email)
        )
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw exception when CPF belongs to another customer")
    void shouldThrowExceptionWhenCpfBelongsToAnotherCustomer() {

        CustomerId id = new CustomerId();
        CPF cpf = new CPF("292.462.720-64");
        Email email = new Email("testando@email.com");

        when(customerRepository.existsByCpfAndIdNot(cpf, id))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.validateForUpdate(id, cpf, email)
        )
                .isInstanceOf(CpfAlreadyExistsException.class);

        verify(customerRepository)
                .existsByCpfAndIdNot(cpf, id);

        verify(customerRepository, never())
                .existsByEmailAndIdNot(any(), any());
    }

    @Test
    @DisplayName("Should throw exception when email belongs to another customer")
    void shouldThrowExceptionWhenEmailBelongsToAnotherCustomer() {

        CustomerId id = new CustomerId();
        CPF cpf = new CPF("292.462.720-64");
        Email email = new Email("testando@email.com");

        when(customerRepository.existsByCpfAndIdNot(cpf, id))
                .thenReturn(false);

        when(customerRepository.existsByEmailAndIdNot(email, id))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.validateForUpdate(id, cpf, email)
        )
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Should find customer by id")
    void shouldFindCustomerById() {

        Customer customer =
                CustomerTestFactory.customer();

        when(customerRepository.findById(
                customer.getId()
        )).thenReturn(Optional.of(customer));

        Customer result =
                service.findCustomer(customer.getId());

        assertThat(result)
                .isSameAs(customer);
    }

    @Test
    @DisplayName("Should throw exception when customer is not found")
    void shouldThrowExceptionWhenCustomerIsNotFound() {

        CustomerId id =
                new CustomerId();

        when(customerRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.findCustomer(id)
        )
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw exception when customer has open credits")
    void shouldThrowExceptionWhenCustomerHasOpenCredits() {

        CustomerId id =
                new CustomerId();

        when(creditRepository.hasOpenCredits(id))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.validateNoOpenCredits(id)
        )
                .isInstanceOf(CustomerHasOpenCreditsException.class);
    }

    @Test
    @DisplayName("Should allow operation when customer has no open credits")
    void shouldAllowOperationWhenCustomerHasNoOpenCredits() {

        CustomerId id =
                new CustomerId();

        when(creditRepository.hasOpenCredits(id))
                .thenReturn(false);

        assertThatCode(() ->
                service.validateNoOpenCredits(id)
        )
                .doesNotThrowAnyException();
    }
}