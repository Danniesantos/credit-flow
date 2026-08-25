package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.exceptions.InstallmentAlreadyPaidException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.InstallmentTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static com.daniela.creditflow.support.TestConstants.TEST_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class InstallmentTest {

    private static final LocalDate TODAY =
            LocalDate.of(2026, 8, 24);

    @Test
    @DisplayName("Should create pending installment")
    void shouldCreatePendingInstallment() {

        Installment installment =
                InstallmentTestFactory.pendingInstallment();

        assertThat(installment.getStatus())
                .isEqualTo(InstallmentStatus.PENDING);

        assertThat(installment.getPaymentMethod())
                .isNull();

        assertThat(installment.getPaidAt())
                .isNull();
    }

    @Test
    @DisplayName("Should pay pending installment")
    void shouldPayPendingInstallment() {

        Installment installment =
                InstallmentTestFactory.pendingInstallment();

        Instant paidAt = Instant.now();

        installment.pay(
                PaymentMethod.PIX,
                paidAt
        );

        assertThat(installment.isPaid())
                .isTrue();

        assertThat(installment.getPaymentMethod())
                .isEqualTo(PaymentMethod.PIX);

        assertThat(installment.getPaidAt())
                .isEqualTo(paidAt);
    }

    @Test
    @DisplayName("Should not pay installment twice")
    void shouldNotPayInstallmentTwice() {

        Installment installment =
                InstallmentTestFactory.paidInstallment();

        assertThatThrownBy(() ->
                installment.pay(
                        PaymentMethod.CREDIT_CARD,
                        Instant.now()
                ))
                .isInstanceOf(
                        InstallmentAlreadyPaidException.class
                );
    }

    @Test
    @DisplayName("Should not create installment with invalid number")
    void shouldNotCreateInstallmentWithInvalidNumber() {

        assertThatThrownBy(() ->
                new Installment(
                        0,
                        TestConstants.INSTALLMENT_AMOUNT,
                        TEST_DATE.plusDays(10),
                        new CreditId()
                ))
                .isInstanceOf(
                        InvalidDomainStateException.class
                )
                .hasMessage(
                        "Installment number must be greater than zero"
                );
    }

    @Test
    @DisplayName("Should not create installment with negative number")
    void shouldNotCreateInstallmentWithNegativeNumber() {

        assertThatThrownBy(() ->
                new Installment(
                        -1,
                        TestConstants.INSTALLMENT_AMOUNT,
                        TEST_DATE.plusDays(10),
                        new CreditId()
                ))
                .isInstanceOf(
                        InvalidDomainStateException.class
                );
    }

    @Test
    @DisplayName("Should identify pending installment")
    void shouldIdentifyPendingInstallment() {

        Installment installment =
                InstallmentTestFactory.pendingInstallment();

        assertThat(installment.isPending())
                .isTrue();
    }

    @Test
    @DisplayName("Should not pay installment that is not pending")
    void shouldNotPayInstallmentThatIsNotPending() {

        Installment installment =
                InstallmentTestFactory.paidInstallment();

        assertThatThrownBy(() ->
                installment.pay(
                        PaymentMethod.PIX,
                        TestConstants.PAID_AT
                ))
                .isInstanceOf(
                        InstallmentAlreadyPaidException.class
                );
    }

    @Test
    @DisplayName("Should identify overdue installment")
    void shouldIdentifyOverdueInstallment() {

        Installment installment =
                InstallmentTestFactory.overdueInstallments(
                        new CreditId(),
                        1
                ).getFirst();

        assertThat(installment.isOverdue(TODAY))
                .isTrue();
    }

    @Test
    @DisplayName("Should calculate overdue days")
    void shouldCalculateOverdueDays() {

        Installment installment =
                InstallmentTestFactory.overdueInstallments(
                        new CreditId(),
                        1
                ).getFirst();

        assertThat(installment.daysOverdue(TODAY))
                .isGreaterThan(0);
    }

    @Test
    @DisplayName("Should not be overdue when due date is in the future")
    void shouldNotBeOverdueWhenDueDateIsFuture() {

        Installment installment =
                InstallmentTestFactory.pendingInstallment();

        assertThat(installment.isOverdue(TODAY))
                .isFalse();

        assertThat(installment.daysOverdue(TODAY))
                .isZero();
    }

}