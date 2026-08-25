package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeactivateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @Transactional
    public void execute(CustomerId customerId) {

        Customer customer = customerService
                .findCustomer(customerId);

        customerService
                .validateNoOpenCredits(customerId);

        customer.deactivate();

        customerRepository.save(customer);

    }
}
