package com.daniela.creditflow.domain.installment.model;

import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.exceptions.DomainException;
import com.daniela.creditflow.domain.installment.valueObject.InstallmentId;
import com.daniela.creditflow.domain.installment.valueObject.PaymentMethod;
import com.daniela.creditflow.domain.valueObject.Money;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Getter
public class Installment {

    private final InstallmentId id;
    private final Integer number;
    private final Money amount;
    private final LocalDate dueDate;
    private PaymentMethod paymentMethod;
    private InstallmentStatus status;
    private final CreditId creditId;
    private Instant paidAt;

    public Installment(Integer number,
                       Money amount,
                       LocalDate dueDate,
                       CreditId creditId) {

        this(
                new InstallmentId(),
                number,
                amount,
                dueDate,
                InstallmentStatus.PENDING,
                creditId
        );
    }

    public Installment(InstallmentId id,
                       Integer number,
                       Money amount,
                       LocalDate dueDate,
                       InstallmentStatus status,
                       CreditId creditId) {

        this.id = Objects.requireNonNull(id);
        this.number = Objects.requireNonNull(number);
        this.amount = Objects.requireNonNull(amount);
        this.dueDate = Objects.requireNonNull(dueDate);
        this.status = Objects.requireNonNull(status);
        this.creditId = Objects.requireNonNull(creditId);

        validateNumber();
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

    public void pay(PaymentMethod paymentMethod,
                    Instant paidAt) {

        if (isPaid()) {
            throw new DomainException(
                    "Installment already paid"
            );
        }

        if (!isPending()) {
            throw new DomainException(
                    "Only pending installments can be paid"
            );
        }

        this.status = InstallmentStatus.PAID;
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.paidAt = Objects.requireNonNull(paidAt);
    }

}

