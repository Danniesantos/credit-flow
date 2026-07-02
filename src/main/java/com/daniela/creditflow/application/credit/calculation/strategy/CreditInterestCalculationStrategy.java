package com.daniela.creditflow.application.credit.calculation.strategy;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.domain.valueObject.Money;

public interface CreditInterestCalculationStrategy {

    CreditCalculationResult calculate(
            Money requestedAmount,
            Integer installments);
}
