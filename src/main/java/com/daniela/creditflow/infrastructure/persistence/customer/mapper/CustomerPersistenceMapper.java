package com.daniela.creditflow.infrastructure.persistence.customer.mapper;

import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.domain.valueobject.*;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceMapper {

    private final Clock clock;

    public CustomerEntity toEntity(Customer customer) {

        return new CustomerEntity(
                customer.getId().value(),
                customer.getName(),
                customer.getCpf().value(),
                customer.getEmail().value(),
                customer.getDateOfBirth(),
                customer.getPhoneNumber().value(),
                customer.getMonthlyIncome().value(),
                customer.getCreditScore().value(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    public Customer toDomain(CustomerEntity entity) {

        CustomerData data = new CustomerData(
                entity.getName(),
                new CPF(entity.getCpf()),
                new Email(entity.getEmail()),
                entity.getDateOfBirth(),
                new PhoneNumber(entity.getPhoneNumber()),
                new CreditScore(entity.getCreditScore()),
                new Money(entity.getMonthlyIncome())
        );

        return new Customer(
                new CustomerId(entity.getId()),
                data,
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                clock
        );
    }
}
