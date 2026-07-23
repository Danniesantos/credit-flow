package com.daniela.creditflow.application.credit.service;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.calculation.CreditStrategyFactory;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.valueObject.Money;
import org.springframework.stereotype.Service;

@Service
public class CreditCalculationService {

    private final CreditStrategyFactory strategyFactory;

    public CreditCalculationService(
            CreditStrategyFactory strategyFactory) {

        this.strategyFactory = strategyFactory;
    }

    public CreditCalculationResult calculate(
            CreditType creditType,
            Money requestedAmount,
            Integer installments) {

        return strategyFactory
                .getStrategy(creditType)
                .calculate(
                        requestedAmount,
                        installments);
    }
}

