package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.exceptions.InstallmentAlreadyPaidException;
import com.daniela.creditflow.support.InstallmentTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class InstallmentTest {

    @Test
    @DisplayName("Should create pending installment")
    void shouldCreatePendingInstallment() {

        Installment installment = InstallmentTestFactory.pendingInstallment();

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

        Installment installment = InstallmentTestFactory.pendingInstallment();

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

        Installment installment = InstallmentTestFactory.paidInstallment();

        assertThatThrownBy(() ->
                installment.pay(
                        PaymentMethod.CREDIT_CARD,
                        Instant.now()
                ))
                .isInstanceOf(InstallmentAlreadyPaidException.class);
    }

}