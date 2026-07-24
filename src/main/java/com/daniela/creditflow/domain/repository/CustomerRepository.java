package com.daniela.creditflow.domain.repository;

import com.daniela.creditflow.domain.valueObject.CPF;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import com.daniela.creditflow.domain.valueObject.Email;
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
