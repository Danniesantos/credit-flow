package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.SimulateCreditOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.domain.valueObject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulateCreditUseCase {

    private final CreditCalculationService calculationService;
    private final CreditApplicationMapper creditMapper;

    public SimulateCreditOutput execute(SimulateCreditInput input) {

        Money requestedAmount =
                new Money(
                        input.requestedAmount());

        CreditCalculationResult calculation =
                calculationService.calculate(
                        input.creditType(),
                        requestedAmount,
                        input.installments());

        Money installmentAmount =
                calculation.installmentAmount(
                        input.installments()
                );

        return creditMapper.toSimulateOutput(
                requestedAmount,
                calculation,
                input.installments(),
                installmentAmount
        );
    }
}
