package com.daniela.creditflow.infrastructure.persistence.customer.repository;

import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.customer.repository.CustomerRepository;
import com.daniela.creditflow.domain.customer.valueObject.CPF;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.customer.valueObject.Email;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import com.daniela.creditflow.infrastructure.persistence.customer.mapper.CustomerPersistenceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;
    private final CustomerPersistenceMapper mapper;

    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository,
                                  CustomerPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        CustomerEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {

        return jpaRepository
                .findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByCpf(CPF cpf) {
        return jpaRepository.existsByCpf(cpf.value());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public boolean existsByCpfAndIdNot(CPF cpf, CustomerId id) {
        return jpaRepository.existsByCpfAndIdNot(
                cpf.value(),
                id.value()
        );
    }

    @Override
    public boolean existsByEmailAndIdNot(Email email, CustomerId id) {
        return jpaRepository.existsByEmailAndIdNot(
                email.value(),
                id.value()
        );
    }
}
