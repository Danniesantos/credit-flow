package com.daniela.creditflow.application.credit.calculation;

import com.daniela.creditflow.domain.valueObject.InterestRate;
import com.daniela.creditflow.domain.valueObject.Money;

public record CreditCalculationResult(Money interestAmount,
                                      Money totalAmount,
                                      InterestRate interestRate) {

    public Money installmentAmount(int installments) {
        return totalAmount.divide(installments);
    }
}
