package com.daniela.creditflow.domain.customer.model;

import com.daniela.creditflow.application.exceptions.CustomerAlreadyInactiveException;
import com.daniela.creditflow.domain.customer.valueObject.CPF;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.customer.valueObject.Email;
import com.daniela.creditflow.domain.customer.valueObject.PhoneNumber;
import com.daniela.creditflow.domain.exceptions.DomainException;
import com.daniela.creditflow.domain.valueObject.*;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Getter
public class Customer {

    private final CustomerId id;
    private String name;
    private CPF cpf;
    private Email email;
    private LocalDate dateOfBirth;
    private PhoneNumber phoneNumber;
    private Money monthlyIncome;
    private CreditScore creditScore;
    private CustomerStatus status;
    private final Instant createdAt;
    private Instant updatedAt;


    public Customer(
            CustomerId id,
            CustomerData data,
            CustomerStatus status,
            Instant createdAt,
            Instant updatedAt) {

        this.id = Objects.requireNonNull(id);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);

        changeData(data);
    }
    public Customer(CustomerData data) {

        this(
                new CustomerId(),
                data,
                CustomerStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
    }

    public void update(CustomerData data) {
        changeData(data);
        this.updatedAt = Instant.now();
    }

    private void validateName() {

        if (name.isBlank()) {
            throw new DomainException(
                    "Name cannot be blank");
        }

        if (name.length() < 3) {
            throw new DomainException(
                    "Name must have at least 3 characters");
        }
    }

    private void validateAge() {

        if (dateOfBirth.isAfter(java.time.LocalDate.now())) {
            throw new DomainException(
                    "Date of birth cannot be in the future");
        }
    }

    public void deactivate() {

        if (isInactive()) {
            throw new CustomerAlreadyInactiveException(id);
        }

        this.status = CustomerStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    private void changeData(CustomerData data) {
        this.name = Objects.requireNonNull(data.name());
        this.cpf = Objects.requireNonNull(data.cpf());
        this.email = Objects.requireNonNull(data.email());
        this.dateOfBirth = Objects.requireNonNull(data.dateOfBirth());
        this.phoneNumber = Objects.requireNonNull(data.phoneNumber());
        this.creditScore = Objects.requireNonNull(data.creditScore());
        this.monthlyIncome = Objects.requireNonNull(data.monthlyIncome());
        validateName();
        validateAge();
    }

    public boolean isActive() {
        return status == CustomerStatus.ACTIVE;
    }

    public boolean isInactive() {
        return status == CustomerStatus.INACTIVE;
    }
}
