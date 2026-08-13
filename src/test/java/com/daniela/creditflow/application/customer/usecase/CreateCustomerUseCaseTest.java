package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerDataMapper;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.exceptions.CpfAlreadyExistsException;
import com.daniela.creditflow.domain.exceptions.EmailAlreadyExistsException;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.domain.repository.CustomerRepository;
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
class CreateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerOutputMapper customerOutputMapper;

    @Mock
    private CustomerDataMapper customerDataMapper;

    @InjectMocks
    private CreateCustomerUseCase useCase;

    @Test
    @DisplayName("Should create customer successfully")
    void shouldCreateCustomerSuccessfully() {

        CreateCustomerInput input =
                mock(CreateCustomerInput.class);

        CustomerData customerData =
                CustomerTestFactory.customerData();

        Customer customer =
                new Customer(customerData);

        CustomerOutput expected =
                mock(CustomerOutput.class);

        when(customerDataMapper.from(input))
                .thenReturn(customerData);

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        when(customerOutputMapper.from(customer))
                .thenReturn(expected);

        CustomerOutput result =
                useCase.execute(input);

        assertThat(result)
                .isSameAs(expected);

        verify(customerService)
                .validateForCreate(
                        customerData.cpf(),
                        customerData.email()
                );

        verify(customerRepository)
                .save(any(Customer.class));

        verify(customerOutputMapper)
                .from(customer);
    }

    @Test
    @DisplayName("Should not save customer when cpf already exists")
    void shouldNotSaveCustomerWhenCpfAlreadyExists() {

        CreateCustomerInput input =
                mock(CreateCustomerInput.class);

        CustomerData customerData =
                CustomerTestFactory.customerData();

        when(customerDataMapper.from(input))
                .thenReturn(customerData);

        doThrow(new CpfAlreadyExistsException())
                .when(customerService)
                .validateForCreate(
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
    @DisplayName("Should not save customer when email already exists")
    void shouldNotSaveCustomerWhenEmailAlreadyExists() {

        CreateCustomerInput input =
                mock(CreateCustomerInput.class);

        CustomerData customerData =
                CustomerTestFactory.customerData();

        when(customerDataMapper.from(input))
                .thenReturn(customerData);

        doThrow(new EmailAlreadyExistsException())
                .when(customerService)
                .validateForCreate(
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
}