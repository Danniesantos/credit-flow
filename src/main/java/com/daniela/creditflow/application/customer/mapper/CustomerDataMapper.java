package com.daniela.creditflow.application.customer.mapper;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.domain.valueobject.CreditScore;
import com.daniela.creditflow.domain.valueobject.CPF;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.domain.valueobject.Email;
import com.daniela.creditflow.domain.valueobject.PhoneNumber;
import com.daniela.creditflow.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CustomerDataMapper {

    public CustomerData from(CreateCustomerInput input) {

        return build(
                input.name(),
                input.cpf(),
                input.email(),
                input.dateOfBirth(),
                input.phoneNumber(),
                input.creditScore(),
                input.monthlyIncome()
        );
    }

    public CustomerData from(UpdateCustomerInput input) {

        return build(
                input.name(),
                input.cpf(),
                input.email(),
                input.dateOfBirth(),
                input.phoneNumber(),
                input.creditScore(),
                input.monthlyIncome()
        );
    }

    private CustomerData build(
            String name,
            String cpf,
            String email,
            LocalDate dateOfBirth,
            String phoneNumber,
            Integer creditScore,
            BigDecimal monthlyIncome
    ) {

        return new CustomerData(
                name,
                new CPF(cpf),
                new Email(email),
                dateOfBirth,
                new PhoneNumber(phoneNumber),
                new CreditScore(creditScore),
                new Money(monthlyIncome)
        );
    }
}

