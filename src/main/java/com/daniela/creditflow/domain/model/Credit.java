package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.exceptions.InstallmentNotFoundException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.valueObject.*;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
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
    private final Integer installmentsQuantity;
    private CreditStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Credit(CreditId id,
                  CustomerId customerId,
                  Money requestedAmount,
                  CreditType creditType,
                  InterestRate interestRate,
                  Integer installmentsQuantity,
                  CreditStatus status,
                  Instant createdAt,
                  Instant updatedAt) {

        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.requestedAmount = Objects.requireNonNull(requestedAmount);
        this.installmentsQuantity = Objects.requireNonNull(installmentsQuantity);
        this.installments = new ArrayList<>();
        this.creditType = Objects.requireNonNull(creditType);
        this.interestRate = Objects.requireNonNull(interestRate);
        this.status = Objects.requireNonNull(status);
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
        validateInstallmentsQuantity();
        validateRequestedAmount();

    }

    public static Credit restore(
            CreditId id,
            CustomerId customerId,
            Money requestedAmount,
            CreditType creditType,
            InterestRate interestRate,
            Integer installmentsQuantity,
            CreditStatus status,
            List<Installment> installments,
            Instant createdAt,
            Instant updatedAt
    ) {

        Credit credit = new Credit(
                id,
                customerId,
                requestedAmount,
                creditType,
                interestRate,
                installmentsQuantity,
                status,
                createdAt,
                updatedAt
        );

        credit.installments = new ArrayList<>(installments);

        return credit;
    }

    public boolean isUnderAnalysis() {
        return status == CreditStatus.UNDER_ANALYSIS;
    }

    public boolean isApproved() {
        return status == CreditStatus.APPROVED;
    }

    public boolean isRejected() {
        return status == CreditStatus.REJECTED;
    }

    public boolean isCanceled() {
        return status == CreditStatus.CANCELED;
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

    public void cancel() {
        ensureCancelable();
        changeStatus(CreditStatus.CANCELED);
    }

    public void contract(List<Installment> installments) {

        if (isContracted()) {
            throw new InvalidDomainStateException(
                    "Credit is already contracted");
        }

        if (!isApproved()) {
            throw new InvalidDomainStateException(
                    "Only approved credits can be contracted");
        }

        validateInstallmentList(installments);

        this.installments = new ArrayList<>(installments);

        validateTotalAmount();

        changeStatus(CreditStatus.CONTRACTED);
    }

    public void markInstallmentAsPaid(
            InstallmentId installmentId,
            PaymentMethod paymentMethod,
            Instant paidAt) {

        if (isPaidOff()) {
            throw new InvalidDomainStateException(
                    "Credit is already paid off"
            );
        }

        if (!isContracted()) {
            throw new InvalidDomainStateException(
                    "Credit must be contracted before payments"
            );
        }

        Installment installment =
                findInstallment(installmentId);

        installment.pay(
                paymentMethod,
                paidAt);

        if (areAllInstallmentsPaid()) {
            changeStatus(CreditStatus.PAID_OFF);
        }
    }

    public Money totalInstallmentsAmount() {

        return installments.stream()
                .map(Installment::getAmount)
                .reduce(Money.zero(), Money::add);

    }

    public Money totalPaidAmount() {

        return installments.stream()
                .filter(Installment::isPaid)
                .map(Installment::getAmount)
                .reduce(Money.zero(), Money::add);
    }

    public Money remainingAmount() {

        return totalInstallmentsAmount()
                .subtract(totalPaidAmount());
    }

    public long paidInstallmentsQuantity() {

        return installments.stream()
                .filter(Installment::isPaid)
                .count();
    }

    public int remainingInstallments() {

        return installments.size()
                - (int) paidInstallmentsQuantity();
    }

    public Installment findInstallment(
            InstallmentId installmentId) {

        return installments.stream()
                .filter(i -> i.getId().equals(installmentId))
                .findFirst()
                .orElseThrow(InstallmentNotFoundException::new);
    }

    private void validateRequestedAmount() {

        if (requestedAmount.isZero()) {
            throw new InvalidDomainStateException(
                    "Requested amount must be greater than zero");
        }
    }

    private void validateInstallmentsQuantity() {

        if (installmentsQuantity <= 0) {
            throw new InvalidDomainStateException(
                    "Installments quantity must be greater than zero");
        }

        if (installmentsQuantity > 60) {
            throw new InvalidDomainStateException(
                    "Maximum number of installments is 60");
        }

    }

    private void validateInstallmentList(List<Installment> installments) {

        if (installments.isEmpty()) {
            throw new InvalidDomainStateException(
                    "Credit must contain at least one installment");
        }

    }

    private void validateTotalAmount() {

        if (totalInstallmentsAmount().lessThan(requestedAmount)) {

            throw new InvalidDomainStateException(
                    "Installments total cannot be lower than requested amount");
        }
    }

    private void ensureUnderAnalysis() {

        if (!isUnderAnalysis()) {
            throw new InvalidDomainStateException(
                    "Credit is not under analysis");
        }
    }

    private boolean areAllInstallmentsPaid() {

        return installments.stream()
                .allMatch(Installment::isPaid);
    }

    private void ensureCancelable() {

        if (isCanceled()) {
            throw new InvalidDomainStateException(
                    "Credit is already canceled");
        }

        if (isRejected()) {
            throw new InvalidDomainStateException(
                    "Rejected credits cannot be canceled");
        }

        if (isContracted()) {
            throw new InvalidDomainStateException(
                    "Contracted credits cannot be canceled");
        }

        if (isPaidOff()) {
            throw new InvalidDomainStateException(
                    "Paid off credits cannot be canceled");
        }
    }

    private void changeStatus(CreditStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }
}

