package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.model.Customer;
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
class FindCustomerUseCaseTest {

    @Mock
    private CustomerOutputMapper customerOutputMapper;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private FindCustomerUseCase useCase;

    @Test
    @DisplayName("Should find customer and return output")
    void shouldFindCustomerAndReturnOutput() {

        Customer customer =
                CustomerTestFactory.customer();

        CustomerId customerId =
                customer.getId();

        CustomerOutput expected =
                mock(CustomerOutput.class);

        when(customerService.findCustomer(customerId))
                .thenReturn(customer);

        when(customerOutputMapper.from(customer))
                .thenReturn(expected);

        CustomerOutput result =
                useCase.execute(customerId);

        assertThat(result)
                .isSameAs(expected);

        verify(customerService)
                .findCustomer(customerId);

        verify(customerOutputMapper)
                .from(customer);
    }

    @Test
    @DisplayName("Should propagate exception when customer is not found")
    void shouldPropagateExceptionWhenCustomerIsNotFound() {

        CustomerId customerId =
                new CustomerId();

        when(customerService.findCustomer(customerId))
                .thenThrow(new CustomerNotFoundException());

        assertThatThrownBy(() ->
                useCase.execute(customerId)
        )
                .isInstanceOf(CustomerNotFoundException.class);

        verify(customerOutputMapper, never())
                .from(any());
    }
}