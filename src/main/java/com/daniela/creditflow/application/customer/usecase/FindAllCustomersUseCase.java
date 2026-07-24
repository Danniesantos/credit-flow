package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindAllCustomersUseCase {

    private final CustomerOutputMapper customerOutputMapper;
    private final CustomerRepository customerRepository;

    public Page<CustomerOutput> execute(Pageable pageable) {

        return customerRepository
                .findAll(pageable)
                .map(customerOutputMapper::from);
    }
}
