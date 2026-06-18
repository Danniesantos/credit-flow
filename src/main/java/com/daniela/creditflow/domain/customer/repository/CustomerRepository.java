package com.daniela.creditflow.domain.customer.repository;

import com.daniela.creditflow.domain.customer.valueObject.CPF;
import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.customer.valueObject.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(CustomerId id);

    Page<Customer> findAll(Pageable pageable);

    boolean existsByCpf(CPF cpf);

    boolean existsByEmail(Email email);

    boolean existsByCpfAndIdNot(CPF cpf, CustomerId id);

    boolean existsByEmailAndIdNot(Email email, CustomerId id);

}
