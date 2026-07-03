package com.daniela.creditflow.application.credit.factory;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.calculation.CreditCalculationService;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.application.installment.factory.InstallmentFactory;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.installment.model.Installment;
import com.daniela.creditflow.domain.valueObject.Money;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CreditFactory {

    private final CreditCalculationService calculationService;
    private final InstallmentFactory installmentFactory;
    private final DueDatePolicy dueDatePolicy;

    public CreditFactory(CreditCalculationService calculationService,
                         InstallmentFactory installmentFactory,
                         DueDatePolicy dueDatePolicy) {

        this.calculationService = calculationService;
        this.installmentFactory = installmentFactory;
        this.dueDatePolicy = dueDatePolicy;
    }

    public Credit create(RequestCreditInput input) {

        CreditId creditId = new CreditId();

        CustomerId customerId =
                new CustomerId(input.customerId());

        Money requestedAmount =
                new Money(input.requestedAmount());

        CreditCalculationResult calculation =
                calculationService.calculate(
                        input.creditType(),
                        requestedAmount,
                        input.installments());

        LocalDate referenceDate = LocalDate.now();

        List<Installment> installments =
                installmentFactory.createInstallments(
                        creditId,
                        input.installments(),
                        calculation.totalAmount(),
                        referenceDate,
                        dueDatePolicy
                );

        return new Credit(
                creditId,
                customerId,
                requestedAmount,
                installments,
                input.creditType(),
                calculation.interestRate(),
                input.paymentMethod(),
                CreditStatus.UNDER_ANALYSIS,
                null,
                null
        );
    }
}
