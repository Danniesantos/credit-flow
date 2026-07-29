package com.daniela.creditflow.application.installment.mapper;

import com.daniela.creditflow.application.credit.dto.output.OverdueOutput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.application.installment.dto.output.OverdueInstallmentOutput;
import com.daniela.creditflow.domain.model.Installment;
import org.springframework.stereotype.Component;

@Component
public class InstallmentOutputMapper {

    public InstallmentDetailsOutput toOutput(Installment installment) {

        return new InstallmentDetailsOutput(
                installment.getId().value(),
                installment.getNumber(),
                installment.getAmount().value(),
                installment.getDueDate(),
                installment.getPaymentMethod(),
                installment.getStatus(),
                installment.getPaidAt()
        );
    }

    public OverdueInstallmentOutput toOverdueOutput(Installment installment) {
        return new OverdueInstallmentOutput(
                installment.getId().value(),
                installment.getNumber(),
                installment.getAmount().value(),
                installment.getDueDate(),
                installment.daysOverdue()
        );
    }
}
