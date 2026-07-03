package com.daniela.creditflow.application.installment.mapper;

import com.daniela.creditflow.application.installment.dto.output.InstallmentOutput;
import com.daniela.creditflow.domain.installment.model.Installment;
import org.springframework.stereotype.Component;

@Component
public class InstallmentOutputMapper {

    public InstallmentOutput toOutput(Installment installment) {

        return new InstallmentOutput(
                installment.getId().value(),
                installment.getNumber(),
                installment.getAmount().value(),
                installment.getDueDate(),
                installment.getStatus()
        );
    }
}
