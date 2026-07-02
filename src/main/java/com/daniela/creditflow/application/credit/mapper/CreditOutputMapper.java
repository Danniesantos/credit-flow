package com.daniela.creditflow.application.credit.mapper;

import com.daniela.creditflow.application.credit.dto.output.CreditDetailsOutput;
import com.daniela.creditflow.application.credit.dto.output.RequestCreditOutput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentOutput;
import com.daniela.creditflow.application.installment.mapper.InstallmentOutputMapper;
import com.daniela.creditflow.domain.credit.model.Credit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreditOutputMapper {

    private final InstallmentOutputMapper installmentMapper;

    public CreditOutputMapper(InstallmentOutputMapper installmentMapper) {
        this.installmentMapper = installmentMapper;
    }

    public RequestCreditOutput toRequestOutput(Credit credit) {
        return new RequestCreditOutput(
                credit.getId().value(),
                credit.getCustomerId().value(),
                credit.getRequestedAmount().value(),
                credit.getInstallments().size(),
                credit.getInterestRate().displayValue(),
                credit.getCreditType(),
                credit.getPaymentMethod(),
                credit.getStatus(),
                credit.getCreatedAt(),
                credit.getUpdatedAt());

    }

    public CreditDetailsOutput toDetailsOutput(Credit credit) {

        List<InstallmentOutput> installments =
                credit.getInstallments()
                        .stream()
                        .map(installmentMapper::toOutput)
                        .toList();

        return new CreditDetailsOutput(
                credit.getId().value(),
                credit.getCustomerId().value(),
                credit.getRequestedAmount().value(),
                credit.getInterestRate().displayValue(),
                credit.getCreditType(),
                credit.getPaymentMethod(),
                credit.getStatus(),
                installments,
                credit.getCreatedAt(),
                credit.getUpdatedAt()
        );
    }
}
