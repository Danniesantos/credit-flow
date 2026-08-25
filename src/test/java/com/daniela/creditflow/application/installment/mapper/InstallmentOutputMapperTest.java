package com.daniela.creditflow.application.installment.mapper;

import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.application.installment.dto.output.OverdueInstallmentOutput;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.InstallmentTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class InstallmentOutputMapperTest {

    private final InstallmentOutputMapper mapper =
            new InstallmentOutputMapper();

    private static final LocalDate TODAY =
            LocalDate.of(2026, 8, 24);

    @Test
    @DisplayName("Should map installment to details output")
    void shouldMapInstallmentToDetailsOutput() {

        Installment installment =
                InstallmentTestFactory.pendingInstallment();

        InstallmentDetailsOutput output =
                mapper.toOutput(installment);

        assertThat(output.installmentId())
                .isEqualTo(installment.getId().value());

        assertThat(output.number())
                .isEqualTo(installment.getNumber());

        assertThat(output.amount())
                .isEqualTo(installment.getAmount().value());

        assertThat(output.dueDate())
                .isEqualTo(installment.getDueDate());

        assertThat(output.paymentMethod())
                .isEqualTo(installment.getPaymentMethod());

        assertThat(output.status())
                .isEqualTo(installment.getStatus());

        assertThat(output.paidAt())
                .isEqualTo(installment.getPaidAt());
    }

    @Test
    @DisplayName("Should map paid installment to details output")
    void shouldMapPaidInstallmentToDetailsOutput() {

        Installment installment =
                InstallmentTestFactory.paidInstallment();

        InstallmentDetailsOutput output =
                mapper.toOutput(installment);

        assertThat(output.installmentId())
                .isEqualTo(installment.getId().value());

        assertThat(output.paymentMethod())
                .isEqualTo(PaymentMethod.PIX);

        assertThat(output.status())
                .isEqualTo(installment.getStatus());

        assertThat(output.paidAt())
                .isEqualTo(TestConstants.PAID_AT);
    }

    @Test
    @DisplayName("Should map overdue installment to overdue output")
    void shouldMapOverdueInstallmentToOverdueOutput() {

        CreditId creditId =
                new CreditId();

        Installment installment =
                InstallmentTestFactory.overdueInstallments(
                        creditId,
                        1
                ).getFirst();

        OverdueInstallmentOutput output =
                mapper.toOverdueOutput(
                        installment,
                        TODAY
                );

        assertThat(output.id())
                .isEqualTo(installment.getId().value());

        assertThat(output.number())
                .isEqualTo(installment.getNumber());

        assertThat(output.amount())
                .isEqualTo(installment.getAmount().value());

        assertThat(output.dueDate())
                .isEqualTo(installment.getDueDate());

        assertThat(output.overdueDays())
                .isEqualTo(
                        installment.daysOverdue(TODAY)
                );
    }
}