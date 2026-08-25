package com.daniela.creditflow.application.credit.calculation.strategy;

import com.daniela.creditflow.domain.valueobject.InterestRate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PersonalCreditStrategy extends BaseCreditStrategy {

    private static final InterestRate RATE =
            new InterestRate(
                    BigDecimal.valueOf(0.05));

    @Override
    protected InterestRate rate() {
        return RATE;
    }
}

