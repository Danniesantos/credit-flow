package com.daniela.creditflow.application.customer.service;

import com.daniela.creditflow.domain.exceptions.CpfAlreadyExistsException;
import com.daniela.creditflow.domain.exceptions.CustomerHasOpenCreditsException;
import com.daniela.creditflow.domain.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.exceptions.EmailAlreadyExistsException;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import com.daniela.creditflow.domain.valueobject.CPF;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.domain.valueobject.Email;
import org.springframework.stereotype.Service;

@Service
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
            throw new CpfAlreadyExistsException();
        }
        if (customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

    public void validateForUpdate(CustomerId id,
                                  CPF cpf,
                                  Email email) {

        if (customerRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new CpfAlreadyExistsException();
        }

        if (customerRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistsException();
        }
    }

    public Customer findCustomer(CustomerId customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public void validateNoOpenCredits(CustomerId customerId) {
        if (creditRepository.hasOpenCredits(customerId)) {
            throw new CustomerHasOpenCreditsException();
        }
    }
}
