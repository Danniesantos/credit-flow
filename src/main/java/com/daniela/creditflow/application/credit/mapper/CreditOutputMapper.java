package com.daniela.creditflow.application.credit.mapper;

import com.daniela.creditflow.application.credit.dto.output.CreditDetailsOutput;
import com.daniela.creditflow.application.credit.dto.output.RequestCreditOutput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentOutput;
import com.daniela.creditflow.application.installment.mapper.InstallmentOutputMapper;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.valueObject.InterestRate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class CreditOutputMapper {

    private final InstallmentOutputMapper installmentMapper;

    public CreditOutputMapper(InstallmentOutputMapper installmentMapper) {
        this.installmentMapper = installmentMapper;
    }

    public RequestCreditOutput toCreditOutput(Credit credit) {
        return new RequestCreditOutput(
                credit.getId().value(),
                credit.getCustomerId().value(),
                credit.getRequestedAmount().value(),
                credit.getInstallments().size(),
                formatRate(credit.getInterestRate()),
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
                formatRate(credit.getInterestRate()),
                credit.getCreditType(),
                credit.getPaymentMethod(),
                credit.getStatus(),
                installments,
                credit.getCreatedAt(),
                credit.getUpdatedAt()
        );
    }

    private BigDecimal formatRate(InterestRate rate) {
        return rate.percentage()
                .setScale(2, RoundingMode.HALF_UP);
    }
}
