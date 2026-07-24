package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import com.daniela.creditflow.infrastructure.web.request.CustomerRequest;
import com.daniela.creditflow.infrastructure.web.response.CustomerResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerWebMapper {

    public CreateCustomerInput toInput(CustomerRequest request) {
        return new CreateCustomerInput(
                request.getName(),
                request.getCpf(),
                request.getEmail(),
                request.getDateOfBirth(),
                request.getPhoneNumber(),
                request.getMonthlyIncome(),
                request.getCreditScore()
        );
    }

    public CustomerResponse toResponse(CustomerOutput output) {
        return new CustomerResponse(
                output.id(),
                output.name(),
                output.cpf(),
                output.email(),
                output.dateOfBirth(),
                output.phoneNumber(),
                output.monthlyIncome(),
                output.creditScore(),
                output.status(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    public UpdateCustomerInput toUpdateInput(UUID id,
                                             CustomerRequest request) {

        return new UpdateCustomerInput(
                id,
                request.getName(),
                request.getCpf(),
                request.getEmail(),
                request.getDateOfBirth(),
                request.getPhoneNumber(),
                request.getMonthlyIncome(),
                request.getCreditScore()
        );
    }

    public CustomerId toCustomerId(UUID id) {
        return new CustomerId(id);
    }
}
