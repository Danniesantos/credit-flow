package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.CustomerValidationService;
import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerDataMapper;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.customer.model.CustomerData;
import com.daniela.creditflow.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerValidationService customerValidationService;
    private final CustomerOutputMapper customerOutputMapper;
    private final CustomerDataMapper customerDataMapper;

    public CreateCustomerUseCase(CustomerRepository customerRepository,
                                 CustomerValidationService customerValidationService,
                                 CustomerOutputMapper customerOutputMapper,
                                 CustomerDataMapper customerDataMapper) {

        this.customerRepository = customerRepository;
        this.customerValidationService = customerValidationService;
        this.customerOutputMapper = customerOutputMapper;
        this.customerDataMapper = customerDataMapper;
    }

    public CustomerOutput execute(CreateCustomerInput input) {

        CustomerData customerData = customerDataMapper.from(input);

        customerValidationService.validateForCreate(
                customerData.cpf(),
                customerData.email()
        );

        Customer customer = new Customer(customerData);

        return customerOutputMapper.from(
                customerRepository.save(customer)
        );
    }
}
