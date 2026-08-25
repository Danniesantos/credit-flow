package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.exceptions.InstallmentNotFoundException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.valueobject.*;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
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
    private Integer installmentsQuantity;
    private CreditStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Credit(
            CreditId id,
            CustomerId customerId,
            Money requestedAmount,
            CreditType creditType,
            InterestRate interestRate,
            Integer installmentsQuantity,
            Instant now) {

        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.requestedAmount = Objects.requireNonNull(requestedAmount);
        this.creditType = Objects.requireNonNull(creditType);
        this.interestRate = Objects.requireNonNull(interestRate);
        this.installmentsQuantity =
                Objects.requireNonNull(installmentsQuantity);
        this.status = CreditStatus.UNDER_ANALYSIS;

        this.installments = new ArrayList<>();

        this.createdAt = Objects.requireNonNull(now);
        this.updatedAt = now;

        validateInstallmentsQuantity();
        validateRequestedAmount();
    }

    public static Credit restore(CreditSnapshot snapshot) {

        Credit credit = new Credit(
                snapshot.id(),
                snapshot.customerId(),
                snapshot.requestedAmount(),
                snapshot.creditType(),
                snapshot.interestRate(),
                snapshot.installmentsQuantity(),
                snapshot.createdAt()
        );

        credit.status = Objects.requireNonNull(snapshot.status());
        credit.installments = new ArrayList<>(snapshot.installments());

        credit.updatedAt = snapshot.updatedAt() != null
                ? snapshot.updatedAt()
                : snapshot.createdAt();

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

    public void approve(Instant now) {
        ensureUnderAnalysis();
        changeStatus(CreditStatus.APPROVED, now);
    }

    public void reject(Instant now) {
        ensureUnderAnalysis();
        changeStatus(CreditStatus.REJECTED, now);
    }

    public void cancel(Instant now) {
        ensureCancelable();
        changeStatus(CreditStatus.CANCELED, now);
    }

    public void contract(List<Installment> installments,
                         Instant now) {

        if (isContracted()) {
            throw new InvalidDomainStateException(
                    "Credit is already contracted"
            );
        }

        if (!isApproved()) {
            throw new InvalidDomainStateException(
                    "Only approved credits can be contracted"
            );
        }

        validateInstallmentList(installments);

        this.installments = new ArrayList<>(installments);

        validateTotalAmount();

        changeStatus(
                CreditStatus.CONTRACTED,
                now);
    }

    public void renegotiate(List<Installment> installments,
                            LocalDate today,
                            Instant now) {

        ensureCanBeRenegotiated(today);

        replaceInstallments(installments, now);
    }

    public void restructure(List<Installment> installments,
                            Instant now) {

        ensureCanBeRestructured();

        replaceInstallments(installments, now);
    }

    private void replaceInstallments(
            List<Installment> newInstallments,
            Instant now) {

        validateInstallmentList(newInstallments);

        List<Installment> updatedInstallments =
                new ArrayList<>(paidInstallments());

        updatedInstallments.addAll(newInstallments);

        this.installments = updatedInstallments;
        this.installmentsQuantity = updatedInstallments.size();

        changeStatus(
                CreditStatus.CONTRACTED,
                now);
    }

    public void markInstallmentAsPaid(InstallmentId installmentId,
                                      PaymentMethod paymentMethod,
                                      Instant paidAt,
                                      Instant now) {

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
                paidAt
        );

        if (areAllInstallmentsPaid()) {
            changeStatus(
                    CreditStatus.PAID_OFF,
                    now
            );
        }
    }

    public Money totalInstallmentsAmount() {

        return installments.stream()
                .map(Installment::getAmount)
                .reduce(
                        Money.zero(),
                        Money::add
                );
    }

    public Money totalPaidAmount() {

        return installments.stream()
                .filter(Installment::isPaid)
                .map(Installment::getAmount)
                .reduce(
                        Money.zero(),
                        Money::add
                );
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

        return (int) installments.stream()
                .filter(Installment::isPending)
                .count();
    }

    public Installment findInstallment(
            InstallmentId installmentId) {

        return installments.stream()
                .filter(i -> i.getId().equals(installmentId))
                .findFirst()
                .orElseThrow(
                        InstallmentNotFoundException::new
                );
    }

    public List<Installment> overdueInstallments(LocalDate today) {

        return installments.stream()
                .filter(installment -> installment.isOverdue(today))
                .toList();
    }

    public boolean hasOverdueInstallments(LocalDate today) {

        return installments.stream()
                .anyMatch(installment -> installment.isOverdue(today));
    }

    public long overdueInstallmentsQuantity(LocalDate today) {

        return overdueInstallments(today).size();
    }

    public Money overdueAmount(LocalDate today) {

        return overdueInstallments(today).stream()
                .map(Installment::getAmount)
                .reduce(
                        Money.zero(),
                        Money::add
                );
    }

    public List<Installment> pendingInstallments() {

        return installments.stream()
                .filter(Installment::isPending)
                .toList();
    }

    public List<Installment> paidInstallments() {

        return installments.stream()
                .filter(Installment::isPaid)
                .toList();
    }

    public boolean hasPendingInstallments() {

        return installments.stream()
                .anyMatch(Installment::isPending);
    }

    public boolean canRenegotiate(LocalDate today) {

        return isContracted()
                && hasOverdueInstallments(today);
    }

    public boolean canRestructure() {

        return isContracted()
                && hasPendingInstallments();
    }

    public void ensureCanBeRenegotiated(LocalDate today) {

        if (!canRenegotiate(today)) {
            throw new InvalidDomainStateException(
                    "Credit cannot be renegotiated"
            );
        }
    }

    public void ensureCanBeRestructured() {

        if (!canRestructure()) {
            throw new InvalidDomainStateException(
                    "Credit cannot be restructured"
            );
        }
    }

    public int nextInstallmentNumber() {

        return installments.stream()
                .mapToInt(Installment::getNumber)
                .max()
                .orElse(0) + 1;
    }

    private void validateRequestedAmount() {

        if (requestedAmount.isZero()) {
            throw new InvalidDomainStateException(
                    "Requested amount must be greater than zero"
            );
        }
    }

    private void validateInstallmentsQuantity() {

        if (installmentsQuantity <= 0) {
            throw new InvalidDomainStateException(
                    "Installments quantity must be greater than zero"
            );
        }

        if (installmentsQuantity > 60) {
            throw new InvalidDomainStateException(
                    "Maximum number of installments is 60"
            );
        }
    }

    private void validateInstallmentList(
            List<Installment> installments) {

        if (installments == null || installments.isEmpty()) {

            throw new InvalidDomainStateException(
                    "Credit must contain at least one installment"
            );
        }
    }

    private void validateTotalAmount() {

        if (totalInstallmentsAmount()
                .lessThan(requestedAmount)) {

            throw new InvalidDomainStateException(
                    "Installments total cannot be lower than requested amount"
            );
        }
    }

    private void ensureUnderAnalysis() {

        if (!isUnderAnalysis()) {
            throw new InvalidDomainStateException(
                    "Credit is not under analysis"
            );
        }
    }

    private boolean areAllInstallmentsPaid() {

        return installments.stream()
                .allMatch(Installment::isPaid);
    }

    private void ensureCancelable() {

        if (isCanceled()) {
            throw new InvalidDomainStateException(
                    "Credit is already canceled"
            );
        }

        if (isRejected()) {
            throw new InvalidDomainStateException(
                    "Rejected credits cannot be canceled"
            );
        }

        if (isContracted()) {
            throw new InvalidDomainStateException(
                    "Contracted credits cannot be canceled"
            );
        }

        if (isPaidOff()) {
            throw new InvalidDomainStateException(
                    "Paid off credits cannot be canceled"
            );
        }
    }

    private void changeStatus(CreditStatus newStatus,
                              Instant now) {

        this.status = newStatus;
        this.updatedAt = now;
    }
}

