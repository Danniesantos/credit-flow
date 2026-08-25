package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.exceptions.CustomerHasOpenCreditsException;
import com.daniela.creditflow.domain.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerStatus;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeactivateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private DeactivateCustomerUseCase useCase;

    @Test
    @DisplayName("Should deactivate customer")
    void shouldDeactivateCustomer() {

        Customer customer =
                CustomerTestFactory.customer();

        CustomerId customerId =
                customer.getId();

        when(customerService.findCustomer(customerId))
                .thenReturn(customer);

        useCase.execute(customerId);

        assertThat(customer.getStatus())
                .isEqualTo(CustomerStatus.INACTIVE);

        verify(customerService)
                .validateNoOpenCredits(customerId);

        verify(customerRepository)
                .save(customer);
    }

    @Test
    @DisplayName("Should not deactivate customer with open credits")
    void shouldNotDeactivateCustomerWithOpenCredits() {

        Customer customer =
                CustomerTestFactory.customer();

        CustomerId customerId =
                customer.getId();

        when(customerService.findCustomer(customerId))
                .thenReturn(customer);

        doThrow(new CustomerHasOpenCreditsException())
                .when(customerService)
                .validateNoOpenCredits(customerId);

        assertThatThrownBy(() ->
                useCase.execute(customerId)
        )
                .isInstanceOf(
                        CustomerHasOpenCreditsException.class
                );

        assertThat(customer.getStatus())
                .isNotEqualTo(CustomerStatus.INACTIVE);

        verify(customerRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Should not deactivate when customer is not found")
    void shouldNotDeactivateWhenCustomerIsNotFound() {

        CustomerId customerId =
                new CustomerId();

        when(customerService.findCustomer(customerId))
                .thenThrow(new CustomerNotFoundException());

        assertThatThrownBy(() ->
                useCase.execute(customerId)
        )
                .isInstanceOf(CustomerNotFoundException.class);

        verify(customerService, never())
                .validateNoOpenCredits(any());

        verify(customerRepository, never())
                .save(any());
    }
}