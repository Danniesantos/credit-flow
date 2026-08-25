package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindCustomerUseCase {

    private final CustomerOutputMapper customerOutputMapper;
    private final CustomerService customerService;

    public CustomerOutput execute(CustomerId customerId) {

        Customer customer =
                customerService.
                        findCustomer(customerId);

        return customerOutputMapper.from(customer);
    }
}
