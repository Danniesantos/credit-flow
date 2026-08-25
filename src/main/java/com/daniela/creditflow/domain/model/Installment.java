package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.exceptions.InstallmentAlreadyPaidException;
import com.daniela.creditflow.domain.exceptions.InstallmentCannotBePaidException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.InstallmentId;
import com.daniela.creditflow.domain.valueobject.Money;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public Installment(
            Integer number,
            Money amount,
            LocalDate dueDate,
            CreditId creditId) {

        this(
                new InstallmentId(),
                number,
                amount,
                dueDate,
                null,
                InstallmentStatus.PENDING,
                creditId,
                null
        );
    }

    private Installment(
            InstallmentId id,
            Integer number,
            Money amount,
            LocalDate dueDate,
            PaymentMethod paymentMethod,
            InstallmentStatus status,
            CreditId creditId,
            Instant paidAt) {

        this.id = Objects.requireNonNull(id);
        this.number = Objects.requireNonNull(number);
        this.amount = Objects.requireNonNull(amount);
        this.dueDate = Objects.requireNonNull(dueDate);
        this.paymentMethod = paymentMethod;
        this.status = Objects.requireNonNull(status);
        this.creditId = Objects.requireNonNull(creditId);
        this.paidAt = paidAt;

        validateNumber();
    }

    public static Installment restore(
            InstallmentSnapshot snapshot) {

        return new Installment(
                snapshot.id(),
                snapshot.number(),
                snapshot.amount(),
                snapshot.dueDate(),
                snapshot.paymentMethod(),
                snapshot.status(),
                snapshot.creditId(),
                snapshot.paidAt()
        );
    }

    public boolean isPaid() {
        return status == InstallmentStatus.PAID;
    }

    public boolean isPending() {
        return status == InstallmentStatus.PENDING;
    }

    public boolean isOverdue(LocalDate today) {
        return isPending() && dueDate.isBefore(today);
    }

    private void validateNumber() {

        if (number <= 0) {
            throw new InvalidDomainStateException(
                    "Installment number must be greater than zero");
        }
    }

    public void pay(PaymentMethod paymentMethod,
                    Instant paidAt) {

        if (isPaid()) {
            throw new InstallmentAlreadyPaidException(
            );
        }

        if (!isPending()) {
            throw new InstallmentCannotBePaidException(
            );
        }
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.paidAt = Objects.requireNonNull(paidAt);
        this.status = InstallmentStatus.PAID;
    }

    public long daysOverdue(LocalDate today) {
        return isOverdue(today)
                ? ChronoUnit.DAYS.between(dueDate, today)
                : 0;
    }

}

