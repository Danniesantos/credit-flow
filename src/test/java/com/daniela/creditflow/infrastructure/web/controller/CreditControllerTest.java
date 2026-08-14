package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.*;
import com.daniela.creditflow.application.credit.usecase.*;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import com.daniela.creditflow.domain.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.model.InstallmentStatus;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.infrastructure.web.mapper.CreditWebMapper;
import com.daniela.creditflow.infrastructure.web.request.CreditAdjustmentRequest;
import com.daniela.creditflow.infrastructure.web.request.RequestCreditRequest;
import com.daniela.creditflow.infrastructure.web.request.SimulateCreditRequest;
import com.daniela.creditflow.infrastructure.web.response.*;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditController.class)
public class CreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreditWebMapper creditWebMapper;

    @MockitoBean
    private SimulateCreditUseCase simulateCreditUseCase;

    @MockitoBean
    private RequestCreditUseCase requestCreditUseCase;

    @MockitoBean
    private AnalyzeCreditUseCase analyzeCreditUseCase;

    @MockitoBean
    private FindCreditUseCase findCreditUseCase;

    @MockitoBean
    private FindCreditBalanceUseCase balanceUseCase;

    @MockitoBean
    private FindDebtorsUseCase debtorsUseCase;

    @MockitoBean
    private ContractCreditUseCase contractUseCase;

    @MockitoBean
    private CancelCreditUseCase cancelUseCase;

    @MockitoBean
    private FindCreditOverdueUseCase overdueUseCase;

    @MockitoBean
    private RenegotiateCreditUseCase renegotiateUseCase;

    @MockitoBean
    private RestructureCreditUseCase restructureUseCase;

    @Test
    @DisplayName("Should request credit successfully")
    void shouldRequestCreditSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        RequestCreditInput input =
                new RequestCreditInput(
                        customerId,
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        24,
                        CreditType.PERSONAL
                );

        RequestCreditOutput output =
                new RequestCreditOutput(
                        creditId,
                        customerId,
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        24,
                        new BigDecimal("0.05"),
                        CreditType.PERSONAL,
                        CreditStatus.UNDER_ANALYSIS,
                        Instant.now(),
                        Instant.now()
                );

        RequestCreditResponse response =
                new RequestCreditResponse(
                        output.creditId(),
                        output.customerId(),
                        output.requestedAmount(),
                        output.installments(),
                        output.interestRate(),
                        output.creditType(),
                        output.status(),
                        output.createdAt(),
                        output.updatedAt()
                );

        when(creditWebMapper.toInput(any(RequestCreditRequest.class)))
                .thenReturn(input);

        when(requestCreditUseCase.execute(input))
                .thenReturn(output);

        when(creditWebMapper.toRequestResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": %s,
                                          "installments": 24,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(
                                        customerId,
                                        TestConstants.TOTAL_CREDIT_AMOUNT.value()
                                ))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        org.hamcrest.Matchers.endsWith(
                                "/credits/" + creditId
                        )
                ))
                .andExpect(jsonPath("$.creditId")
                        .value(creditId.toString()))
                .andExpect(jsonPath("$.customerId")
                        .value(customerId.toString()))
                .andExpect(jsonPath("$.requestedAmount")
                        .value(TestConstants.TOTAL_CREDIT_AMOUNT.value().doubleValue()))
                .andExpect(jsonPath("$.installments")
                        .value(24))
                .andExpect(jsonPath("$.creditType")
                        .value("PERSONAL"))
                .andExpect(jsonPath("$.status")
                        .value(CreditStatus.UNDER_ANALYSIS.name()));

        verify(creditWebMapper)
                .toInput(any(RequestCreditRequest.class));

        verify(requestCreditUseCase)
                .execute(input);

        verify(creditWebMapper)
                .toRequestResponse(output);
    }

    @Test
    @DisplayName("Should simulate credit successfully")
    void shouldSimulateCreditSuccessfully() throws Exception {

        SimulateCreditInput input =
                new SimulateCreditInput(
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        10,
                        CreditType.PERSONAL
                );

        SimulateCreditOutput output =
                new SimulateCreditOutput(
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        TestConstants.FIVE_PERCENT.value(),
                        new BigDecimal("10500"),
                        10,
                        new BigDecimal("1050")
                );

        SimulateCreditResponse response =
                new SimulateCreditResponse(
                        output.requestedAmount(),
                        output.interestRate(),
                        output.totalAmount(),
                        output.installments(),
                        output.installmentAmount()
                );

        when(creditWebMapper.toSimulateInput(any(SimulateCreditRequest.class)))
                .thenReturn(input);

        when(simulateCreditUseCase.execute(input))
                .thenReturn(output);

        when(creditWebMapper.toSimulateResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "requestedAmount": %s,
                                          "installments": 10,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(
                                        TestConstants.TOTAL_CREDIT_AMOUNT.value()
                                ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestedAmount")
                        .value(output.requestedAmount().doubleValue()))
                .andExpect(jsonPath("$.interestRate")
                        .value(output.interestRate().doubleValue()))
                .andExpect(jsonPath("$.totalAmount")
                        .value(output.totalAmount().doubleValue()))
                .andExpect(jsonPath("$.installments")
                        .value(output.installments()))
                .andExpect(jsonPath("$.installmentAmount")
                        .value(output.installmentAmount().doubleValue()));

        verify(creditWebMapper)
                .toSimulateInput(any(SimulateCreditRequest.class));

        verify(simulateCreditUseCase)
                .execute(input);

        verify(creditWebMapper)
                .toSimulateResponse(output);
    }

    @Test
    @DisplayName("Should return bad request when customer id is null")
    void shouldReturnBadRequestWhenCustomerIdIsNull()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": null,
                                          "requestedAmount": 10000,
                                          "installments": 12,
                                          "creditType": "PERSONAL"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when requested amount is null")
    void shouldReturnBadRequestWhenRequestedAmountIsNull()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": null,
                                          "installments": 12,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when requested amount is zero")
    void shouldReturnBadRequestWhenRequestedAmountIsZero()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": 0,
                                          "installments": 12,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when requested amount is negative")
    void shouldReturnBadRequestWhenRequestedAmountIsNegative()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": -100,
                                          "installments": 12,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments are null")
    void shouldReturnBadRequestWhenInstallmentsAreNull()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": 10000,
                                          "installments": null,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should analyze credit successfully")
    void shouldAnalyzeCreditSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        AnalyzeCreditOutput output =
                new AnalyzeCreditOutput(
                        creditId,
                        CreditStatus.APPROVED,
                        null
                );

        AnalyzeCreditResponse response =
                new AnalyzeCreditResponse(
                        output.creditId(),
                        output.status(),
                        output.reason()
                );

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(analyzeCreditUseCase.execute(input))
                .thenReturn(output);

        when(creditWebMapper.toAnalyzeResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        post("/credits/{id}/analyze", creditId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creditId")
                        .value(creditId.toString()))
                .andExpect(jsonPath("$.status")
                        .value(CreditStatus.APPROVED.name()))
                .andExpect(jsonPath("$.reason")
                        .doesNotExist());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(analyzeCreditUseCase)
                .execute(input);

        verify(creditWebMapper)
                .toAnalyzeResponse(output);
    }

    @Test
    @DisplayName("Should return rejected credit after analysis")
    void shouldReturnRejectedCreditAfterAnalysis() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        AnalyzeCreditOutput output =
                new AnalyzeCreditOutput(
                        creditId,
                        CreditStatus.REJECTED,
                        "Customer does not meet the credit requirements"
                );

        AnalyzeCreditResponse response =
                new AnalyzeCreditResponse(
                        output.creditId(),
                        output.status(),
                        output.reason()
                );

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(analyzeCreditUseCase.execute(input))
                .thenReturn(output);

        when(creditWebMapper.toAnalyzeResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        post("/credits/{id}/analyze", creditId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creditId")
                        .value(creditId.toString()))
                .andExpect(jsonPath("$.status")
                        .value(CreditStatus.REJECTED.name()))
                .andExpect(jsonPath("$.reason")
                        .value("Customer does not meet the credit requirements"));

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(analyzeCreditUseCase)
                .execute(input);

        verify(creditWebMapper)
                .toAnalyzeResponse(output);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist")
    void shouldReturnNotFoundWhenCreditDoesNotExistOnAnalyze()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(analyzeCreditUseCase.execute(input))
                .thenThrow(new CreditNotFoundException());

        mockMvc.perform(
                        post("/credits/{id}/analyze", creditId)
                )
                .andExpect(status().isNotFound());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(analyzeCreditUseCase)
                .execute(input);

        verifyNoMoreInteractions(creditWebMapper);
    }

    @Test
    @DisplayName("Should return not found when customer does not exist")
    void shouldReturnNotFoundWhenCustomerDoesNotExistOnAnalyze()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(analyzeCreditUseCase.execute(input))
                .thenThrow(new CustomerNotFoundException());

        mockMvc.perform(
                        post("/credits/{id}/analyze", creditId)
                )
                .andExpect(status().isNotFound());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(analyzeCreditUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit is not under analysis")
    void shouldReturnUnprocessableEntityWhenCreditIsNotUnderAnalysis()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Credit is not under analysis"
        ))
                .when(analyzeCreditUseCase)
                .execute(input);

        mockMvc.perform(
                        post("/credits/{id}/analyze", creditId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(analyzeCreditUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should contract credit successfully")
    void shouldContractCreditSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doNothing()
                .when(contractUseCase)
                .execute(input);

        mockMvc.perform(
                        post("/credits/{id}/contract", creditId)
                )
                .andExpect(status().isNoContent());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(contractUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist")
    void shouldReturnNotFoundWhenCreditDoesNotExistOnContract()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new CreditNotFoundException())
                .when(contractUseCase)
                .execute(input);

        mockMvc.perform(
                        post("/credits/{id}/contract", creditId)
                )
                .andExpect(status().isNotFound());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(contractUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid")
    void shouldReturnBadRequestWhenCreditIdIsInvalidOnContract()
            throws Exception {

        mockMvc.perform(
                        post("/credits/{id}/contract", "invalid-uuid")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(contractUseCase);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit is already contracted")
    void shouldReturnUnprocessableEntityWhenCreditIsAlreadyContracted()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Credit is already contracted"
        ))
                .when(contractUseCase)
                .execute(input);

        mockMvc.perform(
                        post("/credits/{id}/contract", creditId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(contractUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit is not approved")
    void shouldReturnUnprocessableEntityWhenCreditIsNotApproved()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Only approved credits can be contracted"
        ))
                .when(contractUseCase)
                .execute(input);

        mockMvc.perform(
                        post("/credits/{id}/contract", creditId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(contractUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should find credit by id successfully")
    void shouldFindCreditByIdSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID installmentId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        Instant now = Instant.now();

        InstallmentDetailsOutput installment =
                new InstallmentDetailsOutput(
                        installmentId,
                        1,
                        TestConstants.INSTALLMENT_AMOUNT.value(),
                        TestConstants.TEST_DATE,
                        PaymentMethod.PIX,
                        InstallmentStatus.PENDING,
                        null
                );

        CreditDetailsOutput output =
                new CreditDetailsOutput(
                        creditId,
                        customerId,
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        TestConstants.FIVE_PERCENT.value(),
                        CreditType.PERSONAL,
                        CreditStatus.CONTRACTED,
                        List.of(installment),
                        now,
                        now
                );

        CreditDetailsResponse response =
                new CreditDetailsResponse(
                        creditId,
                        customerId,
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        TestConstants.FIVE_PERCENT.value(),
                        CreditType.PERSONAL,
                        CreditStatus.CONTRACTED,
                        List.of(
                                new InstallmentDetailsResponse(
                                        installmentId,
                                        1,
                                        TestConstants.INSTALLMENT_AMOUNT.value(),
                                        TestConstants.TEST_DATE,
                                        PaymentMethod.PIX,
                                        InstallmentStatus.PENDING,
                                        null
                                )
                        ),
                        now,
                        now
                );

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(findCreditUseCase.execute(input))
                .thenReturn(output);

        when(creditWebMapper.toDetailsResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        get("/credits/{id}", creditId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creditId")
                        .value(creditId.toString()))
                .andExpect(jsonPath("$.customerId")
                        .value(customerId.toString()))
                .andExpect(jsonPath("$.requestedAmount")
                        .value(10_000))
                .andExpect(jsonPath("$.interestRate")
                        .value(0.05))
                .andExpect(jsonPath("$.creditType")
                        .value("PERSONAL"))
                .andExpect(jsonPath("$.status")
                        .value("CONTRACTED"))
                .andExpect(jsonPath("$.installments").isArray())
                .andExpect(jsonPath("$.installments.length()")
                        .value(1))
                .andExpect(jsonPath("$.installments[0].installmentId")
                        .value(installmentId.toString()))
                .andExpect(jsonPath("$.installments[0].number")
                        .value(1))
                .andExpect(jsonPath("$.installments[0].amount")
                        .value(1_000))
                .andExpect(jsonPath("$.installments[0].status")
                        .value("PENDING"));

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(findCreditUseCase)
                .execute(input);

        verify(creditWebMapper)
                .toDetailsResponse(output);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist")
    void shouldReturnNotFoundWhenCreditDoesNotExist()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(findCreditUseCase.execute(input))
                .thenThrow(new CreditNotFoundException());

        mockMvc.perform(
                        get("/credits/{id}", creditId)
                )
                .andExpect(status().isNotFound());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(findCreditUseCase)
                .execute(input);

        verify(creditWebMapper, never())
                .toDetailsResponse(any());
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid")
    void shouldReturnBadRequestWhenCreditIdIsInvalid()
            throws Exception {

        mockMvc.perform(
                        get("/credits/{id}", "invalid-uuid")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(findCreditUseCase);
    }

    @Test
    @DisplayName("Should find credit balance successfully")
    void shouldFindCreditBalanceSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        BalanceOutput output =
                new BalanceOutput(
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        TestConstants.INSTALLMENT_AMOUNT.value(),
                        BigDecimal.valueOf(9_000),
                        9
                );

        BalanceResponse response =
                new BalanceResponse(
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        TestConstants.INSTALLMENT_AMOUNT.value(),
                        BigDecimal.valueOf(9_000),
                        9
                );

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(balanceUseCase.execute(input))
                .thenReturn(output);

        when(creditWebMapper.toBalanceResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        get("/credits/{id}/balance", creditId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalContractAmount")
                        .value(10_000))
                .andExpect(jsonPath("$.paidAmount")
                        .value(1_000))
                .andExpect(jsonPath("$.remainingAmount")
                        .value(9_000))
                .andExpect(jsonPath("$.remainingInstallments")
                        .value(9));

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(balanceUseCase)
                .execute(input);

        verify(creditWebMapper)
                .toBalanceResponse(output);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist")
    void shouldReturnNotFoundWhenCreditDoesNotExistOnBalance()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(balanceUseCase.execute(input))
                .thenThrow(new CreditNotFoundException());

        mockMvc.perform(
                        get("/credits/{id}/balance", creditId)
                )
                .andExpect(status().isNotFound());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(balanceUseCase)
                .execute(input);

        verify(creditWebMapper, never())
                .toBalanceResponse(any());
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid")
    void shouldReturnBadRequestWhenCreditIdIsInvalidOnBalance()
            throws Exception {

        mockMvc.perform(
                        get("/credits/{id}/balance", "invalid-uuid")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(balanceUseCase);
    }

    @Test
    @DisplayName("Should find credit overdue status successfully")
    void shouldFindCreditOverdueStatusSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        OverdueOutput output =
                new OverdueOutput(
                        true,
                        2L,
                        BigDecimal.valueOf(2_000),
                        List.of()
                );

        OverdueResponse response =
                new OverdueResponse(
                        true,
                        2L,
                        BigDecimal.valueOf(2_000),
                        List.of()
                );

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(overdueUseCase.execute(input))
                .thenReturn(output);

        when(creditWebMapper.toOverdueResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        get("/credits/{id}/overdue", creditId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasOverdueInstallments")
                        .value(true))
                .andExpect(jsonPath("$.overdueInstallmentsQuantity")
                        .value(2))
                .andExpect(jsonPath("$.overdueAmount")
                        .value(2_000))
                .andExpect(jsonPath("$.installments")
                        .isArray());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(overdueUseCase)
                .execute(input);

        verify(creditWebMapper)
                .toOverdueResponse(output);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist")
    void shouldReturnNotFoundWhenCreditDoesNotExistOnOverdue()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        when(overdueUseCase.execute(input))
                .thenThrow(new CreditNotFoundException());

        mockMvc.perform(
                        get("/credits/{id}/overdue", creditId)
                )
                .andExpect(status().isNotFound());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(overdueUseCase)
                .execute(input);

        verify(creditWebMapper, never())
                .toOverdueResponse(any());
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid")
    void shouldReturnBadRequestWhenCreditIdIsInvalidOnOverdue()
            throws Exception {

        mockMvc.perform(
                        get("/credits/{id}/overdue", "invalid-uuid")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(overdueUseCase);
    }

    @Test
    @DisplayName("Should find debtors successfully")
    void shouldFindDebtorsSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        DebtorOutput first =
                new DebtorOutput(
                        creditId,
                        customerId,
                        2L,
                        BigDecimal.valueOf(2_000)
                );

        DebtorOutput second =
                new DebtorOutput(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        1L,
                        BigDecimal.valueOf(1_000)
                );

        Page<DebtorOutput> output =
                new PageImpl<>(
                        List.of(first, second)
                );

        DebtorResponse firstResponse =
                new DebtorResponse(
                        first.creditId(),
                        first.customerId(),
                        first.overdueInstallments(),
                        first.overdueAmount()
                );

        DebtorResponse secondResponse =
                new DebtorResponse(
                        second.creditId(),
                        second.customerId(),
                        second.overdueInstallments(),
                        second.overdueAmount()
                );

        when(debtorsUseCase.execute(any(Pageable.class)))
                .thenReturn(output);

        when(creditWebMapper.toDebtorResponse(first))
                .thenReturn(firstResponse);

        when(creditWebMapper.toDebtorResponse(second))
                .thenReturn(secondResponse);

        mockMvc.perform(
                        get("/credits/debtors")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()")
                        .value(2))
                .andExpect(jsonPath("$.content[0].creditId")
                        .value(first.creditId().toString()))
                .andExpect(jsonPath("$.content[0].customerId")
                        .value(first.customerId().toString()))
                .andExpect(jsonPath("$.content[0].overdueInstallments")
                        .value(2))
                .andExpect(jsonPath("$.content[0].overdueAmount")
                        .value(2_000))
                .andExpect(jsonPath("$.content[1].creditId")
                        .value(second.creditId().toString()))
                .andExpect(jsonPath("$.content[1].customerId")
                        .value(second.customerId().toString()));

        verify(debtorsUseCase)
                .execute(any(Pageable.class));

        verify(creditWebMapper)
                .toDebtorResponse(first);

        verify(creditWebMapper)
                .toDebtorResponse(second);
    }

    @Test
    @DisplayName("Should return empty page when there are no debtors")
    void shouldReturnEmptyPageWhenThereAreNoDebtors()
            throws Exception {

        when(debtorsUseCase.execute(any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(
                        get("/credits/debtors")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(debtorsUseCase)
                .execute(any(Pageable.class));

        verifyNoInteractions(creditWebMapper);
    }

    @Test
    @DisplayName("Should cancel credit successfully")
    void shouldCancelCreditSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doNothing()
                .when(cancelUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/credits/{id}/cancel", creditId)
                )
                .andExpect(status().isNoContent());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(cancelUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit is already canceled")
    void shouldReturnUnprocessableEntityWhenCreditIsAlreadyCanceled()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Credit is already canceled"
        ))
                .when(cancelUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/credits/{id}/cancel", creditId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(cancelUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when rejected credit is canceled")
    void shouldReturnUnprocessableEntityWhenRejectedCreditIsCanceled()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Rejected credits cannot be canceled"
        ))
                .when(cancelUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/credits/{id}/cancel", creditId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(cancelUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when contracted credit is canceled")
    void shouldReturnUnprocessableEntityWhenContractedCreditIsCanceled()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Contracted credits cannot be canceled"
        ))
                .when(cancelUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/credits/{id}/cancel", creditId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(cancelUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when paid off credit is canceled")
    void shouldReturnUnprocessableEntityWhenPaidOffCreditIsCanceled()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Paid off credits cannot be canceled"
        ))
                .when(cancelUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/credits/{id}/cancel", creditId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(cancelUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist")
    void shouldReturnNotFoundWhenCreditDoesNotExistOnCancel()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId input = new CreditId(creditId);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(input);

        doThrow(new CreditNotFoundException())
                .when(cancelUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/credits/{id}/cancel", creditId)
                )
                .andExpect(status().isNotFound());

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(cancelUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid")
    void shouldReturnBadRequestWhenCreditIdIsInvalidOnCancel()
            throws Exception {

        mockMvc.perform(
                        patch("/credits/{id}/cancel", "invalid-uuid")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(cancelUseCase);
    }

    @Test
    @DisplayName("Should renegotiate credit successfully")
    void shouldRenegotiateCreditSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doNothing()
                .when(renegotiateUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/renegotiate", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isNoContent());

        verify(creditWebMapper)
                .toCreditAdjustmentInput(
                        any(CreditAdjustmentRequest.class)
                );

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(renegotiateUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist on renegotiate")
    void shouldReturnNotFoundWhenCreditDoesNotExistOnRenegotiate()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doThrow(new CreditNotFoundException())
                .when(renegotiateUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/renegotiate", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isNotFound());

        verify(renegotiateUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit cannot be renegotiated")
    void shouldReturnUnprocessableEntityWhenCreditCannotBeRenegotiated()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doThrow(new InvalidDomainStateException(
                "Credit cannot be renegotiated"
        ))
                .when(renegotiateUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/renegotiate", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(renegotiateUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit has no installments")
    void shouldReturnUnprocessableEntityWhenCreditHasNoInstallments()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doThrow(new InvalidDomainStateException(
                "Credit must contain at least one installment"
        ))
                .when(renegotiateUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/renegotiate", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(renegotiateUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid on renegotiate")
    void shouldReturnBadRequestWhenCreditIdIsInvalidOnRenegotiate()
            throws Exception {

        mockMvc.perform(
                        post("/credits/{id}/renegotiate", "invalid-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(renegotiateUseCase);
    }

    @Test
    @DisplayName("Should restructure credit successfully")
    void shouldRestructureCreditSuccessfully() throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doNothing()
                .when(restructureUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/restructure", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isNoContent());

        verify(creditWebMapper)
                .toCreditAdjustmentInput(
                        any(CreditAdjustmentRequest.class)
                );

        verify(creditWebMapper)
                .toCreditId(creditId);

        verify(restructureUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist on restructure")
    void shouldReturnNotFoundWhenCreditDoesNotExistOnRestructure()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doThrow(new CreditNotFoundException())
                .when(restructureUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/restructure", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isNotFound());

        verify(restructureUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit cannot be restructured")
    void shouldReturnUnprocessableEntityWhenCreditCannotBeRestructured()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doThrow(new InvalidDomainStateException(
                "Credit cannot be restructured"
        ))
                .when(restructureUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/restructure", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(restructureUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit has no installments")
    void shouldReturnUnprocessableEntityWhenCreditHasNoInstallmentsOnRestructure()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        CreditId inputCreditId = new CreditId(creditId);

        CreditAdjustmentInput input =
                new CreditAdjustmentInput(24);

        when(creditWebMapper.toCreditAdjustmentInput(
                any(CreditAdjustmentRequest.class)
        )).thenReturn(input);

        when(creditWebMapper.toCreditId(creditId))
                .thenReturn(inputCreditId);

        doThrow(new InvalidDomainStateException(
                "Credit must contain at least one installment"
        ))
                .when(restructureUseCase)
                .execute(inputCreditId, input);

        mockMvc.perform(
                        post("/credits/{id}/restructure", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(restructureUseCase)
                .execute(inputCreditId, input);
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid on restructure")
    void shouldReturnBadRequestWhenCreditIdIsInvalidOnRestructure()
            throws Exception {

        mockMvc.perform(
                        post("/credits/{id}/restructure", "invalid-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "installmentsQuantity": 24
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(restructureUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments are less than one")
    void shouldReturnBadRequestWhenInstallmentsAreLessThanOne()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": 10000,
                                          "installments": 0,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments exceed sixty")
    void shouldReturnBadRequestWhenInstallmentsExceedSixty()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": 10000,
                                          "installments": 61,
                                          "creditType": "PERSONAL"
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when credit type is null")
    void shouldReturnBadRequestWhenCreditTypeIsNull()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": 10000,
                                          "installments": 12,
                                          "creditType": null
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when customer id has invalid format")
    void shouldReturnBadRequestWhenCustomerIdHasInvalidFormat()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "invalid-uuid",
                                          "requestedAmount": 10000,
                                          "installments": 12,
                                          "creditType": "PERSONAL"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when credit type is invalid")
    void shouldReturnBadRequestWhenCreditTypeIsInvalid()
            throws Exception {

        mockMvc.perform(
                        post("/credits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "customerId": "%s",
                                          "requestedAmount": 10000,
                                          "installments": 12,
                                          "creditType": "INVALID"
                                        }
                                        """.formatted(UUID.randomUUID()))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(requestCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when requested amount is null")
    void shouldReturnBadRequestWhenRequestedAmountIsNullOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": null,
                                      "installments": 12,
                                      "creditType": "PERSONAL"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when requested amount is zero")
    void shouldReturnBadRequestWhenRequestedAmountIsZeroOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": 0,
                                      "installments": 12,
                                      "creditType": "PERSONAL"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when requested amount is negative")
    void shouldReturnBadRequestWhenRequestedAmountIsNegativeOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": -100,
                                      "installments": 12,
                                      "creditType": "PERSONAL"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments are null")
    void shouldReturnBadRequestWhenInstallmentsAreNullOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": 10000,
                                      "installments": null,
                                      "creditType": "PERSONAL"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments are less than one")
    void shouldReturnBadRequestWhenInstallmentsAreLessThanOneOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": 10000,
                                      "installments": 0,
                                      "creditType": "PERSONAL"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments exceed sixty")
    void shouldReturnBadRequestWhenInstallmentsExceedSixtyOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": 10000,
                                      "installments": 61,
                                      "creditType": "PERSONAL"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when credit type is null")
    void shouldReturnBadRequestWhenCreditTypeIsNullOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": 10000,
                                      "installments": 12,
                                      "creditType": null
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when credit type is invalid")
    void shouldReturnBadRequestWhenCreditTypeIsInvalidOnSimulation()
            throws Exception {

        mockMvc.perform(
                        post("/credits/simulate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "requestedAmount": 10000,
                                      "installments": 12,
                                      "creditType": "INVALID"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(simulateCreditUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments quantity is null on renegotiation")
    void shouldReturnBadRequestWhenInstallmentsQuantityIsNullOnRenegotiation()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        mockMvc.perform(
                        post("/credits/{id}/renegotiate", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "installmentsQuantity": null
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(renegotiateUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments quantity is less than two on renegotiation")
    void shouldReturnBadRequestWhenInstallmentsQuantityIsLessThanTwoOnRenegotiation()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        mockMvc.perform(
                        post("/credits/{id}/renegotiate", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "installmentsQuantity": 1
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(renegotiateUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments quantity is null on restructure")
    void shouldReturnBadRequestWhenInstallmentsQuantityIsNullOnRestructure()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        mockMvc.perform(
                        post("/credits/{id}/restructure", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "installmentsQuantity": null
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(restructureUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installments quantity is less than two on restructure")
    void shouldReturnBadRequestWhenInstallmentsQuantityIsLessThanTwoOnRestructure()
            throws Exception {

        UUID creditId = UUID.randomUUID();

        mockMvc.perform(
                        post("/credits/{id}/restructure", creditId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "installmentsQuantity": 1
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(creditWebMapper);
        verifyNoInteractions(restructureUseCase);
    }
}
