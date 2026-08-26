package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.usecase.PayInstallmentUseCase;
import com.daniela.creditflow.infrastructure.web.mapper.InstallmentWebMapper;
import com.daniela.creditflow.infrastructure.web.request.PaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Installments",
        description = "Operations related to installments"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/installments")
public class InstallmentController {

    private final PayInstallmentUseCase payInstallmentUseCase;
    private final InstallmentWebMapper mapper;

    @Operation(
            summary = "Pay installment",
            description = "Processes the payment of an installment."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Installment successfully paid"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid payment data or UUID format",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    @ApiResponse(
            responseCode = "404",
            description = "Credit or installment not found",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    @ApiResponse(
            responseCode = "422",
            description = "Payment failed or installment cannot be paid due to an invalid credit state",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    @PostMapping("/{installmentId}/pay")
    public ResponseEntity<Void> pay(
            @Parameter(
                    description = "Installment unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID installmentId,
            @RequestBody @Valid PaymentRequest request
    ) {

        PaymentInstallmentInput input =
                mapper.toPaymentInstallmentInput(request, installmentId);

        payInstallmentUseCase.execute(input);

        return ResponseEntity.noContent().build();
    }
}
