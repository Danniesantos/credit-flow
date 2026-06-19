package com.daniela.creditflow.application.customer.service;

import com.daniela.creditflow.application.exceptions.CustomerHasOpenCreditsException;
import com.daniela.creditflow.application.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.credit.repository.CreditRepository;
import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.customer.repository.CustomerRepository;
import com.daniela.creditflow.domain.exceptions.CpfAlreadyExistsException;
import com.daniela.creditflow.domain.exceptions.EmailAlreadyExistsException;
import com.daniela.creditflow.domain.customer.valueObject.CPF;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.customer.valueObject.Email;
import org.springframework.stereotype.Component;

@Component
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CreditRepository creditRepository;

    public CustomerService(CustomerRepository customerRepository,
                           CreditRepository creditRepository) {
        this.customerRepository = customerRepository;
        this.creditRepository = creditRepository;
    }

    public void validateForCreate(CPF cpf,
                                   Email email) {

        if (customerRepository.existsByCpf(cpf)) {
            throw new CpfAlreadyExistsException(cpf.value());
        }
        if (customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email.value());
        }
    }

    public void validateForUpdate(CustomerId id,
                                   CPF cpf,
                                   Email email) {

        if (customerRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new CpfAlreadyExistsException(cpf.value());
        }

        if (customerRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistsException(email.value());
        }
    }

    public Customer findCustomer(CustomerId customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public void validateNoOpenCredits(CustomerId customerId) {
        if (creditRepository.hasOpenCredits(customerId)) {
            throw new CustomerHasOpenCreditsException(customerId);
        }
    }
}
