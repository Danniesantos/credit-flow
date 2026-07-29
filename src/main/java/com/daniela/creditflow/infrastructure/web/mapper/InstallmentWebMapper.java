package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.credit.dto.output.OverdueOutput;
import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.application.installment.dto.output.OverdueInstallmentOutput;
import com.daniela.creditflow.domain.valueObject.InstallmentId;
import com.daniela.creditflow.infrastructure.web.request.PaymentRequest;
import com.daniela.creditflow.infrastructure.web.response.InstallmentDetailsResponse;
import com.daniela.creditflow.infrastructure.web.response.OverdueInstallmentResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InstallmentWebMapper {

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

    public OverdueInstallmentResponse toOverdueInstallmentResponse(OverdueInstallmentOutput output) {
        return new OverdueInstallmentResponse(
                output.id(),
                output.number(),
                output.amount(),
                output.dueDate(),
                output.overdueDays()
        );
    }

    public InstallmentId toInstallmentId(UUID id) {
        return new InstallmentId(id);
    }
}
