package com.daniela.creditflow.application.credit.calculation.strategy;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.domain.valueobject.InterestRate;
import com.daniela.creditflow.domain.valueobject.Money;

public abstract class BaseCreditStrategy implements CreditInterestCalculationStrategy {

    protected abstract InterestRate rate();

    @Override
    public CreditCalculationResult calculate(
            Money requestedAmount,
            Integer installments) {

        InterestRate rate = rate();

        Money interestAmount =
                rate.calculateInterest(
                        requestedAmount);

        Money totalAmount =
                requestedAmount.add(
                        interestAmount);

        return new CreditCalculationResult(
                interestAmount,
                totalAmount,
                rate
        );
    }
}
