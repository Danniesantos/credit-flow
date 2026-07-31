package com.daniela.creditflow.application.credit.service;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.installment.factory.InstallmentFactory;
import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditInstallmentService {

    private final CreditCalculationService calculationService;
    private final InstallmentFactory installmentFactory;
    private final DueDatePolicy dueDatePolicy;


    public List<Installment> generate(Credit credit,
                                      Integer quantity) {

        CreditCalculationResult calculation =
                calculationService.calculate(
                        credit.getCreditType(),
                        credit.remainingAmount(),
                        quantity
                );

        return installmentFactory.createInstallments(
                credit.getId(),
                credit.nextInstallmentNumber(),
                quantity,
                calculation.totalAmount(),
                LocalDate.now(),
                dueDatePolicy
        );
    }
}
