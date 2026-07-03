package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.installment.dto.input.InstallmentInput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentOutput;
import com.daniela.creditflow.infrastructure.web.request.InstallmentRequest;
import com.daniela.creditflow.infrastructure.web.response.InstallmentDetailsResponse;
import org.springframework.stereotype.Component;

@Component
public class InstallmentWebMapper {

    public InstallmentInput toInput(InstallmentRequest request) {
        return new InstallmentInput(
                request.number(),
                request.amount(),
                request.dueDate()
        );
    }

    public InstallmentDetailsResponse toResponse(InstallmentOutput output) {
        return new InstallmentDetailsResponse(
                output.installmentId(),
                output.number(),
                output.amount(),
                output.dueDate(),
                output.status()
        );
    }
}
