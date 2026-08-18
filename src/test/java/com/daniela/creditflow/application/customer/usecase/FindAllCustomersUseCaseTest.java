package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import com.daniela.creditflow.support.CustomerTestFactory;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllCustomersUseCaseTest {

    @Mock
    private CustomerOutputMapper customerOutputMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private FindAllCustomersUseCase useCase;

    @Test
    @DisplayName("Should return all customers")
    void shouldReturnAllCustomers() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Customer customer =
                CustomerTestFactory.customer();

        CustomerOutput output =
                mock(CustomerOutput.class);

        Page<Customer> customers =
                new PageImpl<>(
                        List.of(customer),
                        pageable,
                        1
                );

        when(customerRepository.findAll(pageable))
                .thenReturn(customers);

        when(customerOutputMapper.from(customer))
                .thenReturn(output);

        Page<CustomerOutput> result =
                useCase.execute(pageable);

        assertThat(result)
                .hasSize(1);

        assertThat(result.getContent().getFirst())
                .isSameAs(output);

        verify(customerRepository)
                .findAll(pageable);

        verify(customerOutputMapper)
                .from(customer);
    }

    @Test
    @DisplayName("Should return empty page when no customers exist")
    void shouldReturnEmptyPageWhenNoCustomersExist() {

        Pageable pageable =
                PageRequest.of(0, 10);

        when(customerRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));

        Page<CustomerOutput> result =
                useCase.execute(pageable);

        assertThat(result)
                .isEmpty();

        verify(customerRepository)
                .findAll(pageable);

        verifyNoInteractions(customerOutputMapper);
    }
}