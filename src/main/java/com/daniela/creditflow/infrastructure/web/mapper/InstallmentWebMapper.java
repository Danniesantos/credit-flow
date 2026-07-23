package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.installment.dto.input.InstallmentInput;
import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.infrastructure.web.request.InstallmentRequest;
import com.daniela.creditflow.infrastructure.web.request.PaymentRequest;
import com.daniela.creditflow.infrastructure.web.response.InstallmentDetailsResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InstallmentWebMapper {

    public InstallmentInput toInput(InstallmentRequest request) {
        return new InstallmentInput(
                request.number(),
                request.amount(),
                request.dueDate()
        );
    }

    public PaymentInstallmentInput toPaymentInstallmentInput(PaymentRequest request,
                                                             UUID installmentId) {
        return new PaymentInstallmentInput(
                request.creditId(),
                installmentId,
                request.paymentMethod()
        );
    }

    public InstallmentDetailsResponse toResponse(InstallmentDetailsOutput output) {
        return new InstallmentDetailsResponse(
                output.installmentId(),
                output.number(),
                output.amount(),
                output.dueDate(),
                output.paymentMethod(),
                output.status(),
                output.paidAt()
        );
    }
}
