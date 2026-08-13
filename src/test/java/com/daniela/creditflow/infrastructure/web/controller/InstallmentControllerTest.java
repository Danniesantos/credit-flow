package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.usecase.PayInstallmentUseCase;
import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import com.daniela.creditflow.domain.exceptions.InstallmentNotFoundException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.infrastructure.web.mapper.InstallmentWebMapper;
import com.daniela.creditflow.infrastructure.web.request.PaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstallmentController.class)
class InstallmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PayInstallmentUseCase payInstallmentUseCase;

    @MockitoBean
    private InstallmentWebMapper mapper;

    @Test
    @DisplayName("Should pay installment successfully")
    void shouldPayInstallmentSuccessfully() throws Exception {

        UUID installmentId = UUID.randomUUID();
        UUID creditId = UUID.randomUUID();

        PaymentInstallmentInput input =
                new PaymentInstallmentInput(
                        creditId,
                        installmentId,
                        PaymentMethod.PIX
                );

        when(mapper.toPaymentInstallmentInput(
                any(PaymentRequest.class),
                eq(installmentId)
        )).thenReturn(input);

        mockMvc.perform(
                        post(
                                "/installments/{installmentId}/pay",
                                installmentId
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "creditId": "%s",
                                          "paymentMethod": "PIX"
                                        }
                                        """.formatted(creditId))
                )
                .andExpect(status().isNoContent());

        verify(mapper).toPaymentInstallmentInput(
                argThat(request ->
                        request.creditId().equals(creditId)
                                && request.paymentMethod()
                                == PaymentMethod.PIX
                ),
                eq(installmentId)
        );

        verify(payInstallmentUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return bad request when payment method is invalid")
    void shouldReturnBadRequestWhenPaymentMethodIsInvalid() throws Exception {

        UUID installmentId = UUID.randomUUID();
        UUID creditId = UUID.randomUUID();

        mockMvc.perform(
                        post("/installments/{installmentId}/pay", installmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "creditId": "%s",
                              "paymentMethod": "INVALID"
                            }
                            """.formatted(creditId))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(mapper);
        verifyNoInteractions(payInstallmentUseCase);
    }

    @Test
    @DisplayName("Should return bad request when credit id is invalid")
    void shouldReturnBadRequestWhenCreditIdIsInvalid() throws Exception {

        UUID installmentId = UUID.randomUUID();

        mockMvc.perform(
                        post("/installments/{installmentId}/pay", installmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "creditId": "invalid-uuid",
                              "paymentMethod": "PIX"
                            }
                            """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(mapper);
        verifyNoInteractions(payInstallmentUseCase);
    }

    @Test
    @DisplayName("Should return bad request when installment id is invalid")
    void shouldReturnBadRequestWhenInstallmentIdIsInvalid() throws Exception {

        UUID creditId = UUID.randomUUID();

        mockMvc.perform(
                        post("/installments/{installmentId}/pay", "invalid-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "creditId": "%s",
                              "paymentMethod": "PIX"
                            }
                            """.formatted(creditId))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(mapper);
        verifyNoInteractions(payInstallmentUseCase);
    }

    @Test
    @DisplayName("Should return not found when credit does not exist")
    void shouldReturnNotFoundWhenCreditDoesNotExist() throws Exception {

        UUID installmentId = UUID.randomUUID();
        UUID creditId = UUID.randomUUID();

        when(mapper.toPaymentInstallmentInput(
                any(PaymentRequest.class),
                eq(installmentId)
        )).thenReturn(
                new PaymentInstallmentInput(
                        creditId,
                        installmentId,
                        PaymentMethod.PIX
                )
        );

        doThrow(new CreditNotFoundException())
                .when(payInstallmentUseCase)
                .execute(any(PaymentInstallmentInput.class));

        mockMvc.perform(
                        post("/installments/{installmentId}/pay", installmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "creditId": "%s",
                              "paymentMethod": "PIX"
                            }
                            """.formatted(creditId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return not found when installment does not exist")
    void shouldReturnNotFoundWhenInstallmentDoesNotExist() throws Exception {

        UUID installmentId = UUID.randomUUID();
        UUID creditId = UUID.randomUUID();

        PaymentInstallmentInput input =
                new PaymentInstallmentInput(
                        creditId,
                        installmentId,
                        PaymentMethod.PIX
                );

        when(mapper.toPaymentInstallmentInput(
                any(PaymentRequest.class),
                eq(installmentId)
        )).thenReturn(input);

        doThrow(new InstallmentNotFoundException())
                .when(payInstallmentUseCase)
                .execute(input);

        mockMvc.perform(
                        post("/installments/{installmentId}/pay", installmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "creditId": "%s",
                                      "paymentMethod": "PIX"
                                    }
                                    """.formatted(creditId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return unprocessable entity when credit is not contracted")
    void shouldReturnUnprocessableEntityWhenCreditIsNotContracted()
            throws Exception {

        UUID installmentId = UUID.randomUUID();
        UUID creditId = UUID.randomUUID();

        PaymentInstallmentInput input =
                new PaymentInstallmentInput(
                        creditId,
                        installmentId,
                        PaymentMethod.PIX
                );

        when(mapper.toPaymentInstallmentInput(
                any(PaymentRequest.class),
                eq(installmentId)
        )).thenReturn(input);

        doThrow(new InvalidDomainStateException(
                "Credit must be contracted before payments"
        ))
                .when(payInstallmentUseCase)
                .execute(input);

        mockMvc.perform(
                        post("/installments/{installmentId}/pay", installmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "creditId": "%s",
                                      "paymentMethod": "PIX"
                                    }
                                    """.formatted(creditId))
                )
                .andExpect(status().isUnprocessableEntity());
    }
}