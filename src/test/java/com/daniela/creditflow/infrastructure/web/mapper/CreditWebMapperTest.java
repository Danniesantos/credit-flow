package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.*;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.application.installment.dto.output.OverdueInstallmentOutput;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.infrastructure.web.request.CreditAdjustmentRequest;
import com.daniela.creditflow.infrastructure.web.request.RequestCreditRequest;
import com.daniela.creditflow.infrastructure.web.request.SimulateCreditRequest;
import com.daniela.creditflow.infrastructure.web.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditWebMapperTest {

    @Mock
    private InstallmentWebMapper installmentMapper;

    @InjectMocks
    private CreditWebMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CreditWebMapper(installmentMapper);
    }

    @Test
    @DisplayName("Should convert request credit request to input")
    void shouldConvertRequestCreditRequestToInput() {

        UUID customerId = UUID.randomUUID();

        RequestCreditRequest request =
                new RequestCreditRequest();

        request.setCustomerId(customerId);
        request.setRequestedAmount(
                new BigDecimal("10000.00")
        );
        request.setInstallments(12);
        request.setCreditType(CreditType.PERSONAL);

        RequestCreditInput result =
                mapper.toInput(request);

        assertThat(result.customerId())
                .isEqualTo(customerId);

        assertThat(result.requestedAmount())
                .isEqualByComparingTo("10000.00");

        assertThat(result.installments())
                .isEqualTo(12);

        assertThat(result.creditType())
                .isEqualTo(CreditType.PERSONAL);
    }

    @Test
    @DisplayName("Should convert simulate credit request to input")
    void shouldConvertSimulateCreditRequestToInput() {

        SimulateCreditRequest request =
                new SimulateCreditRequest();

        request.setRequestedAmount(
                new BigDecimal("10000.00")
        );
        request.setInstallments(12);
        request.setCreditType(CreditType.PERSONAL);

        SimulateCreditInput result =
                mapper.toSimulateInput(request);

        assertThat(result.requestedAmount())
                .isEqualByComparingTo("10000.00");

        assertThat(result.installments())
                .isEqualTo(12);

        assertThat(result.creditType())
                .isEqualTo(CreditType.PERSONAL);
    }

    @Test
    @DisplayName("Should convert UUID to CreditId")
    void shouldConvertUuidToCreditId() {

        UUID id = UUID.randomUUID();

        CreditId result =
                mapper.toCreditId(id);

        assertThat(result.value())
                .isEqualTo(id);
    }

    @Test
    @DisplayName("Should convert request credit output to response")
    void shouldConvertRequestCreditOutputToResponse() {

        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        RequestCreditOutput output =
                new RequestCreditOutput(
                        creditId,
                        customerId,
                        new BigDecimal("10000.00"),
                        12,
                        new BigDecimal("0.05"),
                        CreditType.PERSONAL,
                        CreditStatus.UNDER_ANALYSIS,
                        createdAt,
                        updatedAt
                );

        RequestCreditResponse result =
                mapper.toRequestResponse(output);

        assertThat(result.creditId())
                .isEqualTo(creditId);

        assertThat(result.customerId())
                .isEqualTo(customerId);

        assertThat(result.requestedAmount())
                .isEqualByComparingTo(output.requestedAmount());

        assertThat(result.installments())
                .isEqualTo(output.installments());

        assertThat(result.interestRate())
                .isEqualByComparingTo(output.interestRate());

        assertThat(result.creditType())
                .isEqualTo(output.creditType());

        assertThat(result.status())
                .isEqualTo(output.status());

        assertThat(result.createdAt())
                .isEqualTo(output.createdAt());

        assertThat(result.updatedAt())
                .isEqualTo(output.updatedAt());
    }

    @Test
    @DisplayName("Should convert simulate credit output to response")
    void shouldConvertSimulateCreditOutputToResponse() {

        SimulateCreditOutput output =
                new SimulateCreditOutput(
                        new BigDecimal("10000.00"),
                        new BigDecimal("0.05"),
                        new BigDecimal("10500.00"),
                        12,
                        new BigDecimal("875.00")
                );

        SimulateCreditResponse result =
                mapper.toSimulateResponse(output);

        assertThat(result.requestedAmount())
                .isEqualByComparingTo(output.requestedAmount());

        assertThat(result.interestRate())
                .isEqualByComparingTo(output.interestRate());

        assertThat(result.totalAmount())
                .isEqualByComparingTo(output.totalAmount());

        assertThat(result.installments())
                .isEqualTo(output.installments());

        assertThat(result.installmentAmount())
                .isEqualByComparingTo(output.installmentAmount());
    }

    @Test
    @DisplayName("Should convert analyze credit output to response")
    void shouldConvertAnalyzeCreditOutputToResponse() {

        UUID creditId = UUID.randomUUID();

        AnalyzeCreditOutput output =
                new AnalyzeCreditOutput(
                        creditId,
                        CreditStatus.APPROVED,
                        null
                );

        AnalyzeCreditResponse result =
                mapper.toAnalyzeResponse(output);

        assertThat(result.creditId())
                .isEqualTo(creditId);

        assertThat(result.status())
                .isEqualTo(CreditStatus.APPROVED);

        assertThat(result.reason())
                .isNull();
    }

    @Test
    @DisplayName("Should convert credit details output to response")
    void shouldConvertCreditDetailsOutputToResponse() {

        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        InstallmentDetailsOutput installment =
                new InstallmentDetailsOutput(
                        UUID.randomUUID(),
                        1,
                        new BigDecimal("1000.00"),
                        LocalDate.now().plusMonths(1),
                        PaymentMethod.PIX,
                        InstallmentStatus.PENDING,
                        null
                );

        CreditDetailsOutput output =
                new CreditDetailsOutput(
                        creditId,
                        customerId,
                        new BigDecimal("10000.00"),
                        new BigDecimal("0.05"),
                        CreditType.PERSONAL,
                        CreditStatus.CONTRACTED,
                        List.of(installment),
                        Instant.now(),
                        Instant.now()
                );

        InstallmentDetailsResponse installmentResponse =
                new InstallmentDetailsResponse(
                        installment.installmentId(),
                        installment.number(),
                        installment.amount(),
                        installment.dueDate(),
                        installment.paymentMethod(),
                        installment.status(),
                        installment.paidAt()
                );

        when(installmentMapper.toResponse(installment))
                .thenReturn(installmentResponse);

        CreditDetailsResponse result =
                mapper.toDetailsResponse(output);

        assertThat(result.creditId())
                .isEqualTo(creditId);

        assertThat(result.customerId())
                .isEqualTo(customerId);

        assertThat(result.installments())
                .containsExactly(installmentResponse);

        verify(installmentMapper)
                .toResponse(installment);
    }

    @Test
    @DisplayName("Should convert balance output to response")
    void shouldConvertBalanceOutputToResponse() {

        BalanceOutput output =
                new BalanceOutput(
                        new BigDecimal("12000.00"),
                        new BigDecimal("3000.00"),
                        new BigDecimal("9000.00"),
                        9
                );

        BalanceResponse result =
                mapper.toBalanceResponse(output);

        assertThat(result.totalContractAmount())
                .isEqualByComparingTo(output.totalContractAmount());

        assertThat(result.paidAmount())
                .isEqualByComparingTo(output.paidAmount());

        assertThat(result.remainingAmount())
                .isEqualByComparingTo(output.remainingAmount());

        assertThat(result.remainingInstallments())
                .isEqualTo(output.remainingInstallments());
    }

    @Test
    @DisplayName("Should convert overdue output to response")
    void shouldConvertOverdueOutputToResponse() {

        OverdueInstallmentOutput installment =
                new OverdueInstallmentOutput(
                        UUID.randomUUID(),
                        1,
                        new BigDecimal("1000.00"),
                        LocalDate.now().minusDays(10),
                        10
                );

        OverdueOutput output =
                new OverdueOutput(
                        true,
                        1L,
                        new BigDecimal("1000.00"),
                        List.of(installment)
                );

        OverdueInstallmentResponse installmentResponse =
                new OverdueInstallmentResponse(
                        installment.id(),
                        installment.number(),
                        installment.amount(),
                        installment.dueDate(),
                        installment.overdueDays()
                );

        when(installmentMapper.toOverdueInstallmentResponse(installment))
                .thenReturn(installmentResponse);

        OverdueResponse result =
                mapper.toOverdueResponse(output);

        assertThat(result.hasOverdueInstallments())
                .isTrue();

        assertThat(result.overdueInstallmentsQuantity())
                .isEqualTo(1);

        assertThat(result.overdueAmount())
                .isEqualByComparingTo("1000.00");

        assertThat(result.installments())
                .containsExactly(installmentResponse);

        verify(installmentMapper)
                .toOverdueInstallmentResponse(installment);
    }

    @Test
    @DisplayName("Should convert debtor output to response")
    void shouldConvertDebtorOutputToResponse() {

        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        DebtorOutput output =
                new DebtorOutput(
                        creditId,
                        customerId,
                        2L,
                        new BigDecimal("2000.00")
                );

        DebtorResponse result =
                mapper.toDebtorResponse(output);

        assertThat(result.creditId())
                .isEqualTo(creditId);

        assertThat(result.customerId())
                .isEqualTo(customerId);

        assertThat(result.overdueInstallments())
                .isEqualTo(2);

        assertThat(result.overdueAmount())
                .isEqualByComparingTo("2000.00");
    }

    @Test
    @DisplayName("Should convert credit adjustment request to input")
    void shouldConvertCreditAdjustmentRequestToInput() {

        CreditAdjustmentRequest request =
                new CreditAdjustmentRequest();

        request.setInstallmentsQuantity(12);

        CreditAdjustmentInput result =
                mapper.toCreditAdjustmentInput(request);

        assertThat(result.installmentsQuantity())
                .isEqualTo(12);
    }
}