package com.daniela.creditflow.domain.installment.model;

import com.daniela.creditflow.domain.exceptions.DomainException;
import com.daniela.creditflow.domain.valueObject.InstallmentId;
import com.daniela.creditflow.domain.valueObject.Money;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class Installment {

    private final InstallmentId id;
    private final Integer number;
    private final Money amount;
    private final LocalDate dueDate;
    private InstallmentStatus status;

    public Installment(Integer number,
                       Money amount,
                       LocalDate dueDate) {

        this(
                new InstallmentId(),
                number,
                amount,
                dueDate,
                InstallmentStatus.PENDING);
    }

    public Installment(InstallmentId id,
                       Integer number,
                       Money amount,
                       LocalDate dueDate,
                       InstallmentStatus status) {

        this.id = Objects.requireNonNull(id);
        this.number = Objects.requireNonNull(number);
        this.amount = Objects.requireNonNull(amount);
        this.dueDate = Objects.requireNonNull(dueDate);
        this.status = Objects.requireNonNull(status);

        validateNumber();
    }

    public void markAsPaid() {

        if (isPaid()) {
            throw new DomainException("Installment already paid");
        }

        if (!isPending()) {
            throw new DomainException("Only pending installments can be paid");
        }

        this.status = InstallmentStatus.PAID;
    }

    public boolean isPaid() {
        return status == InstallmentStatus.PAID;
    }

    public boolean isPending() {
        return status == InstallmentStatus.PENDING;
    }

    public boolean isOverdue() {
        return isPending() && dueDate.isBefore(LocalDate.now());
    }

    private void validateNumber() {

        if (number <= 0) {
            throw new DomainException(
                    "Installment number must be greater than zero");
        }
    }
}

