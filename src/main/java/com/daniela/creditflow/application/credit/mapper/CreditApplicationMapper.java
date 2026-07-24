package com.daniela.creditflow.application.credit.mapper;

import com.daniela.creditflow.application.credit.dto.output.BalanceOutput;
import com.daniela.creditflow.application.credit.dto.output.CreditDetailsOutput;
import com.daniela.creditflow.application.credit.dto.output.RequestCreditOutput;
import com.daniela.creditflow.application.installment.dto.output.InstallmentDetailsOutput;
import com.daniela.creditflow.application.installment.mapper.InstallmentOutputMapper;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueObject.InterestRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreditApplicationMapper {

    private final InstallmentOutputMapper installmentMapper;

    public RequestCreditOutput toCreditOutput(Credit credit) {
        return new RequestCreditOutput(
                credit.getId().value(),
                credit.getCustomerId().value(),
                credit.getRequestedAmount().value(),
                credit.getInstallmentsQuantity(),
                formatRate(credit.getInterestRate()),
                credit.getCreditType(),
                credit.getStatus(),
                credit.getCreatedAt(),
                credit.getUpdatedAt());

    }

    public CreditDetailsOutput toDetailsOutput(Credit credit) {

        List<InstallmentDetailsOutput> installments =
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
                credit.getStatus(),
                installments,
                credit.getCreatedAt(),
                credit.getUpdatedAt()
        );
    }

    public BalanceOutput toBalanceOutput(Credit credit) {
        return new BalanceOutput(credit.totalInstallmentsAmount().value(),
                credit.totalPaidAmount().value(),
                credit.remainingAmount().value(),
                credit.remainingInstallments());
    }

    private BigDecimal formatRate(InterestRate rate) {
        return rate.percentage()
                .setScale(2, RoundingMode.HALF_UP);
    }
}