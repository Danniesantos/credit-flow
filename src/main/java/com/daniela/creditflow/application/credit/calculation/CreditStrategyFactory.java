package com.daniela.creditflow.application.credit.calculation;

import com.daniela.creditflow.application.credit.calculation.strategy.BusinessCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.InterestCalculationStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PayrollCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PersonalCreditStrategy;
import com.daniela.creditflow.domain.credit.model.CreditType;
import com.daniela.creditflow.domain.exceptions.DomainException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditStrategyFactory {

    private final Map<CreditType,
            InterestCalculationStrategy> strategies;

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

    public InterestCalculationStrategy getStrategy(
            CreditType type) {

        InterestCalculationStrategy strategy =
                strategies.get(type);

        if (strategy == null) {
            throw new IllegalStateException(
                    "No strategy registered for credit type: " + type);
        }

        return strategy;
    }
}
