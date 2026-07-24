package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import org.springframework.stereotype.Service;

@Service
public class DeactivateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public DeactivateCustomerUseCase(CustomerRepository customerRepository,
                                     CustomerService customerService) {

        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    public void execute(CustomerId customerId) {

        Customer customer = customerService
                .findCustomer(customerId);

        customerService
                .validateNoOpenCredits(customerId);

        customer.deactivate();

        customerRepository.save(customer);

    }
}
