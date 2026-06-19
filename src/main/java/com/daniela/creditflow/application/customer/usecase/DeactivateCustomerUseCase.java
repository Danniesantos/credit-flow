package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.CustomerService;
import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.customer.repository.CustomerRepository;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import org.springframework.stereotype.Service;

@Service
public class DeactivateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerService customerValidationService;

    public DeactivateCustomerUseCase(
            CustomerRepository customerRepository,
            CustomerService customerValidation) {

        this.customerRepository = customerRepository;
        this.customerValidationService = customerValidation;
    }

    public void execute(CustomerId customerId) {

        Customer customer = customerValidationService
                .findCustomer(customerId);

        customerValidationService
                .validateNoOpenCredits(customerId);

        customer.deactivate();

        customerRepository.save(customer);

    }
}
