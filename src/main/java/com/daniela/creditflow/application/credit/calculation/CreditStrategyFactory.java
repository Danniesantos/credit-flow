package com.daniela.creditflow.application.credit.calculation;

import com.daniela.creditflow.application.credit.calculation.strategy.BusinessCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.CreditInterestCalculationStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PayrollCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PersonalCreditStrategy;
import com.daniela.creditflow.domain.model.CreditType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditStrategyFactory {

    private final Map<CreditType,
            CreditInterestCalculationStrategy> strategies;

    public CreditStrategyFactory(
            PersonalCreditStrategy personal,
            PayrollCreditStrategy payroll,
            BusinessCreditStrategy business) {

        this.strategies = Map.of(
                CreditType.PERSONAL, personal,
                CreditType.PAYROLL, payroll,
                CreditType.BUSINESS, business
        );
    }

    public CreditInterestCalculationStrategy getStrategy(CreditType type) {

        if (type == null) {
            throw new IllegalStateException(
                    "Credit type cannot be null"
            );
        }

        CreditInterestCalculationStrategy strategy =
                strategies.get(type);

        if (strategy == null) {
            throw new IllegalStateException(
                    "No strategy registered for credit type: " + type);
        }

        return strategy;
    }
}
