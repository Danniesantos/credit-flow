package com.daniela.creditflow.application.customer.usecase;

import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.mapper.CustomerDataMapper;
import com.daniela.creditflow.application.customer.mapper.CustomerOutputMapper;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.domain.repository.CustomerRepository;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final CustomerOutputMapper customerOutputMapper;
    private final CustomerDataMapper customerDataMapper;

    @Transactional
    public CustomerOutput execute(UpdateCustomerInput input) {

        CustomerData customerData = customerDataMapper.from(input);

        CustomerId customerId = new CustomerId(input.id());

        Customer customer =
                customerService
                        .findCustomer(customerId);

        customerService
                .validateForUpdate(
                        customerId,
                        customerData.cpf(),
                        customerData.email()
                );

        customer.update(customerData);

        return customerOutputMapper.from(
                customerRepository.save(customer)
        );
    }
}
