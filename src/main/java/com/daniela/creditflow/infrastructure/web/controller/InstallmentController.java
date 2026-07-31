package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.usecase.PayInstallmentUseCase;
import com.daniela.creditflow.infrastructure.web.mapper.InstallmentWebMapper;
import com.daniela.creditflow.infrastructure.web.request.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/installments")
public class InstallmentController {

    private final PayInstallmentUseCase payInstallmentUseCase;
    private final InstallmentWebMapper mapper;

    @PostMapping("/{installmentId}/pay")
    public ResponseEntity<Void> pay(@PathVariable UUID installmentId,
                                    @RequestBody PaymentRequest request) {

        PaymentInstallmentInput input =
                mapper.toPaymentInstallmentInput(request, installmentId);

        payInstallmentUseCase.execute(input);

        return ResponseEntity.noContent().build();
    }
}
