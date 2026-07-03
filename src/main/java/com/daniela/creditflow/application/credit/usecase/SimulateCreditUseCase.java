package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.calculation.CreditCalculationService;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.SimulateCreditOutput;
import com.daniela.creditflow.domain.valueObject.Money;
import org.springframework.stereotype.Service;

@Service
public class SimulateCreditUseCase {

    private final CreditCalculationService calculationService;

    public SimulateCreditUseCase(CreditCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public SimulateCreditOutput execute(
            SimulateCreditInput input) {

        Money requestedAmount =
                new Money(
                        input.requestedAmount());

        CreditCalculationResult calculation =
                calculationService.calculate(
                        input.creditType(),
                        requestedAmount,
                        input.installments());

        Money installmentAmount =
                calculation.totalAmount()
                        .divide(input.installments());

        return new SimulateCreditOutput(
                requestedAmount.value(),
                calculation.interestRate().percentage(),
                calculation.totalAmount().value(),
                input.installments(),
                installmentAmount.value()
        );
    }
}
