package com.daniela.creditflow.application.credit.factory;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.domain.valueobject.Money;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

@Component
public class CreditFactory {

    private final CreditCalculationService calculationService;
    private final Clock clock;

    public CreditFactory(
            CreditCalculationService calculationService,
            Clock clock
    ) {
        this.calculationService = calculationService;
        this.clock = clock;
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

        Instant now = clock.instant();

        return new Credit(
                creditId,
                customerId,
                requestedAmount,
                input.creditType(),
                calculation.interestRate(),
                input.installments(),
                now
        );
    }
}
