package com.daniela.creditflow.application.customer.mapper;

import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.domain.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerOutputMapper {

    public CustomerOutput from(Customer customer) {
        return new CustomerOutput(
                customer.getId().value(),
                customer.getName(),
                maskCpf(customer.getCpf().value()),
                customer.getEmail().value(),
                customer.getDateOfBirth(),
                customer.getPhoneNumber().value(),
                customer.getMonthlyIncome().value(),
                customer.getCreditScore().value(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt());
    }

    private String maskCpf(String cpf) {
        return "***.***.***-" + cpf.substring(9);
    }

}
