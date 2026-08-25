package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerDataMapper;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.exceptions.CpfAlreadyExistsException;
import com.daniela.creditflow.domain.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.exceptions.EmailAlreadyExistsException;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerData;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerOutputMapper customerOutputMapper;

    @Mock
    private CustomerDataMapper customerDataMapper;

    @InjectMocks
    private UpdateCustomerUseCase useCase;

    @Test
    @DisplayName("Should update customer successfully")
    void shouldUpdateCustomerSuccessfully() {

        Customer customer =
                CustomerTestFactory.customer();

        UpdateCustomerInput input =
                mock(UpdateCustomerInput.class);

        CustomerData customerData =
                CustomerTestFactory.customerData();

        CustomerOutput expected =
                mock(CustomerOutput.class);

        when(input.id())
                .thenReturn(customer.getId().value());

        when(customerDataMapper.from(input))
                .thenReturn(customerData);

        when(customerService.findCustomer(
                customer.getId()
        )).thenReturn(customer);

        when(customerRepository.save(customer))
                .thenReturn(customer);

        when(customerOutputMapper.from(customer))
                .thenReturn(expected);

        CustomerOutput result =
                useCase.execute(input);

        assertThat(result)
                .isSameAs(expected);

        verify(customerService)
                .findCustomer(customer.getId());

        verify(customerService)
                .validateForUpdate(
                        customer.getId(),
                        customerData.cpf(),
                        customerData.email()
                );

        verify(customerRepository)
                .save(customer);

        verify(customerOutputMapper)
                .from(customer);
    }

    @Test
    @DisplayName("Should not update customer when CPF already exists")
    void shouldNotUpdateCustomerWhenCpfAlreadyExists() {

        Customer customer =
                CustomerTestFactory.customer();

        UpdateCustomerInput input =
                mock(UpdateCustomerInput.class);

        CustomerData customerData =
                CustomerTestFactory.customerData();

        when(input.id())
                .thenReturn(customer.getId().value());

        when(customerDataMapper.from(input))
                .thenReturn(customerData);

        when(customerService.findCustomer(
                customer.getId()
        )).thenReturn(customer);

        doThrow(new CpfAlreadyExistsException())
                .when(customerService)
                .validateForUpdate(
                        customer.getId(),
                        customerData.cpf(),
                        customerData.email()
                );

        assertThatThrownBy(() ->
                useCase.execute(input)
        )
                .isInstanceOf(CpfAlreadyExistsException.class);

        verify(customerRepository, never())
                .save(any());

        verify(customerOutputMapper, never())
                .from(any());
    }

    @Test
    @DisplayName("Should not update customer when email already exists")
    void shouldNotUpdateCustomerWhenEmailAlreadyExists() {

        Customer customer =
                CustomerTestFactory.customer();

        UpdateCustomerInput input =
                mock(UpdateCustomerInput.class);

        CustomerData customerData =
                CustomerTestFactory.customerData();

        when(input.id())
                .thenReturn(customer.getId().value());

        when(customerDataMapper.from(input))
                .thenReturn(customerData);

        when(customerService.findCustomer(
                customer.getId()
        )).thenReturn(customer);

        doThrow(new EmailAlreadyExistsException())
                .when(customerService)
                .validateForUpdate(
                        customer.getId(),
                        customerData.cpf(),
                        customerData.email()
                );

        assertThatThrownBy(() ->
                useCase.execute(input)
        )
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(customerRepository, never())
                .save(any());

        verify(customerOutputMapper, never())
                .from(any());
    }

    @Test
    @DisplayName("Should not update when customer is not found")
    void shouldNotUpdateWhenCustomerIsNotFound() {

        UpdateCustomerInput input =
                mock(UpdateCustomerInput.class);

        CustomerId customerId =
                new CustomerId();

        CustomerData customerData =
                CustomerTestFactory.customerData();

        when(input.id())
                .thenReturn(customerId.value());

        when(customerDataMapper.from(input))
                .thenReturn(customerData);

        when(customerService.findCustomer(customerId))
                .thenThrow(new CustomerNotFoundException());

        assertThatThrownBy(() ->
                useCase.execute(input)
        )
                .isInstanceOf(CustomerNotFoundException.class);

        verify(customerService, never())
                .validateForUpdate(any(), any(), any());

        verify(customerRepository, never())
                .save(any());

        verify(customerOutputMapper, never())
                .from(any());
    }
}