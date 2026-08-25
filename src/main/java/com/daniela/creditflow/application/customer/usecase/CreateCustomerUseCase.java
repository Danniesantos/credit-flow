package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerDataMapper;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final CustomerOutputMapper customerOutputMapper;
    private final CustomerDataMapper customerDataMapper;
    private final Clock clock;

    @Transactional
    public CustomerOutput execute(CreateCustomerInput input) {

        CustomerData customerData = customerDataMapper.from(input);

        customerService.validateForCreate(
                customerData.cpf(),
                customerData.email()
        );

        Customer customer = new Customer(customerData, clock);

        return customerOutputMapper.from(
                customerRepository.save(customer)
        );
    }
}
