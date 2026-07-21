package com.daniela.creditflow.application.credit.factory;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.valueObject.Money;
import org.springframework.stereotype.Component;

@Component
public class CreditFactory {

    private final CreditCalculationService calculationService;

    public CreditFactory(CreditCalculationService calculationService) {

        this.calculationService = calculationService;
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

        return new Credit(
                creditId,
                customerId,
                requestedAmount,
                input.creditType(),
                calculation.interestRate(),
                input.installments(),
                input.paymentMethod(),
                CreditStatus.UNDER_ANALYSIS,
                null,
                null
        );
    }
}
