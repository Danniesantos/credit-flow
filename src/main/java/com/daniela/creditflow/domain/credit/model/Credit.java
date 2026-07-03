package com.daniela.creditflow.domain.credit.model;

import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.exceptions.DomainException;
import com.daniela.creditflow.domain.exceptions.InstallmentNotFoundException;
import com.daniela.creditflow.domain.installment.model.Installment;
import com.daniela.creditflow.domain.valueObject.InstallmentId;
import com.daniela.creditflow.domain.valueObject.InterestRate;
import com.daniela.creditflow.domain.valueObject.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
public class Credit {

    private final CreditId id;
    private final CustomerId customerId;
    private final Money requestedAmount;
    private List<Installment> installments;
    private final CreditType creditType;
    private final InterestRate interestRate;
    private final PaymentMethod paymentMethod;
    private CreditStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Credit(CreditId id,
                  CustomerId customerId,
                  Money requestedAmount,
                  List<Installment> installments,
                  CreditType creditType,
                  InterestRate interestRate,
                  PaymentMethod paymentMethod,
                  CreditStatus status,
                  Instant createdAt,
                  Instant updatedAt) {

        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.requestedAmount = Objects.requireNonNull(requestedAmount);
        this.installments = List.copyOf(
                Objects.requireNonNull(installments));
        this.creditType = Objects.requireNonNull(creditType);
        this.interestRate = Objects.requireNonNull(interestRate);
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.status = Objects.requireNonNull(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validateRequestedAmount();
        validateInstallments();
    }


    public boolean isApproved() {
        return status == CreditStatus.APPROVED;
    }

    public boolean isRejected() {
        return status == CreditStatus.REJECTED;
    }

    public boolean isUnderAnalysis() {
        return status == CreditStatus.UNDER_ANALYSIS;
    }

    public boolean isContracted() {
        return status == CreditStatus.CONTRACTED;
    }

    public boolean isPaidOff() {
        return status == CreditStatus.PAID_OFF;
    }

    public void approve() {
        ensureUnderAnalysis();
        changeStatus(CreditStatus.APPROVED);
    }

    public void reject() {
        ensureUnderAnalysis();
        changeStatus(CreditStatus.REJECTED);
    }

    public void contract() {

        if (!isApproved()) {
            throw new DomainException(
                    "Credit must be approved first");
        }

        changeStatus(CreditStatus.CONTRACTED);
    }

    public void markInstallmentAsPaid(
            InstallmentId installmentId) {

        if (isPaidOff()) {
            throw new DomainException(
                    "Credit is already paid off");
        }

        if (!isContracted()) {
            throw new DomainException(
                    "Credit must be contracted before payments");
        }

        Installment installment =
                findInstallment(installmentId);

        installment.markAsPaid();

        if (installments.stream()
                .allMatch(Installment::isPaid)) {

            changeStatus(CreditStatus.PAID_OFF);
        }
    }

    public Money totalInstallmentsAmount() {

        return installments.stream()
                .map(Installment::getAmount)
                .reduce(Money.zero(), Money::add);

    }

    private Installment findInstallment(
            InstallmentId installmentId) {

        return installments.stream()
                .filter(i -> i.getId().equals(installmentId))
                .findFirst()
                .orElseThrow(() ->
                        new InstallmentNotFoundException(installmentId));
    }

    private void validateRequestedAmount() {

        if (requestedAmount.isZero()) {
            throw new DomainException(
                    "Requested amount must be greater than zero");
        }
    }

    private void validateInstallments() {

        if (installments.isEmpty()) {
            throw new DomainException(
                    "Credit must contain at least one installment");
        }

        if (installments.size() > 60) {
            throw new DomainException(
                    "Maximum number of installments is 60");
        }
    }

    private void validateTotalAmount() {

        if (totalInstallmentsAmount()
                .lessThan(requestedAmount)) {

            throw new DomainException(
                    "Installments total cannot be lower than requested amount");
        }
    }

    private void ensureUnderAnalysis() {

        if (!isUnderAnalysis()) {
            throw new DomainException(
                    "Credit is not under analysis");
        }
    }

    private void changeStatus(CreditStatus newStatus) {
        this.status = newStatus;
    }
}

