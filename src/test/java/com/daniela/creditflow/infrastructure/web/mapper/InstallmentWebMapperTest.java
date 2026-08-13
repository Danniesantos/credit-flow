package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.application.installment.dto.output.OverdueInstallmentOutput;
import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.domain.valueObject.InstallmentId;
import com.daniela.creditflow.infrastructure.web.request.PaymentRequest;
import com.daniela.creditflow.infrastructure.web.response.InstallmentDetailsResponse;
import com.daniela.creditflow.infrastructure.web.response.OverdueInstallmentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InstallmentWebMapperTest {

    private final InstallmentWebMapper mapper =
            new InstallmentWebMapper();

    @Test
    @DisplayName("Should convert payment request to payment installment input")
    void shouldConvertPaymentRequestToInput() {

        UUID creditId = UUID.randomUUID();
        UUID installmentId = UUID.randomUUID();

        PaymentRequest request =
                new PaymentRequest(
                        creditId,
                        PaymentMethod.PIX
                );

        PaymentInstallmentInput result =
                mapper.toPaymentInstallmentInput(
                        request,
                        installmentId
                );

        assertThat(result.creditId())
                .isEqualTo(creditId);

        assertThat(result.installmentId())
                .isEqualTo(installmentId);

        assertThat(result.paymentMethod())
                .isEqualTo(PaymentMethod.PIX);
    }

    @Test
    @DisplayName("Should convert installment details output to response")
    void shouldConvertInstallmentDetailsOutputToResponse() {

        UUID installmentId = UUID.randomUUID();
        LocalDate dueDate = LocalDate.now();
        Instant paidAt = Instant.now();

        InstallmentDetailsOutput output =
                new InstallmentDetailsOutput(
                        installmentId,
                        1,
                        new BigDecimal("500.00"),
                        dueDate,
                        PaymentMethod.PIX,
                        InstallmentStatus.PAID,
                        paidAt
                );

        InstallmentDetailsResponse result =
                mapper.toResponse(output);

        assertThat(result.installmentId())
                .isEqualTo(installmentId);

        assertThat(result.number())
                .isEqualTo(1);

        assertThat(result.amount())
                .isEqualByComparingTo("500.00");

        assertThat(result.dueDate())
                .isEqualTo(dueDate);

        assertThat(result.paymentMethod())
                .isEqualTo(PaymentMethod.PIX);

        assertThat(result.status())
                .isEqualTo(InstallmentStatus.PAID);

        assertThat(result.paidAt())
                .isEqualTo(paidAt);
    }

    @Test
    @DisplayName("Should convert overdue installment output to response")
    void shouldConvertOverdueInstallmentOutputToResponse() {

        UUID installmentId = UUID.randomUUID();
        LocalDate dueDate = LocalDate.now().minusDays(10);

        OverdueInstallmentOutput output =
                new OverdueInstallmentOutput(
                        installmentId,
                        1,
                        new BigDecimal("500.00"),
                        dueDate,
                        10
                );

        OverdueInstallmentResponse result =
                mapper.toOverdueInstallmentResponse(output);

        assertThat(result.id())
                .isEqualTo(installmentId);

        assertThat(result.number())
                .isEqualTo(1);

        assertThat(result.amount())
                .isEqualByComparingTo("500.00");

        assertThat(result.dueDate())
                .isEqualTo(dueDate);

        assertThat(result.overdueDays())
                .isEqualTo(10);
    }

    @Test
    @DisplayName("Should convert UUID to InstallmentId")
    void shouldConvertUuidToInstallmentId() {

        UUID id = UUID.randomUUID();

        InstallmentId result =
                mapper.toInstallmentId(id);

        assertThat(result.value())
                .isEqualTo(id);
    }
}