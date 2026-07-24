package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerDataMapper;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final CustomerOutputMapper customerOutputMapper;
    private final CustomerDataMapper customerDataMapper;

    public CreateCustomerUseCase(CustomerRepository customerRepository,
                                 CustomerService customerService,
                                 CustomerOutputMapper customerOutputMapper,
                                 CustomerDataMapper customerDataMapper) {

        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.customerOutputMapper = customerOutputMapper;
        this.customerDataMapper = customerDataMapper;
    }

    public CustomerOutput execute(CreateCustomerInput input) {

        CustomerData customerData = customerDataMapper.from(input);

        customerService.validateForCreate(
                customerData.cpf(),
                customerData.email()
        );

        Customer customer = new Customer(customerData);

        return customerOutputMapper.from(
                customerRepository.save(customer)
        );
    }
}
