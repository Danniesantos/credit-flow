package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.valueObject.InterestRate;
import com.daniela.creditflow.domain.valueObject.Money;

import java.math.BigDecimal;

public class TestConstants {

    private TestConstants() {
    }

    public static final Money TEN_THOUSAND =
            new Money(BigDecimal.valueOf(10_000));

    public static final Money ONE_THOUSAND =
            new Money(BigDecimal.valueOf(1_000));

    public static final InterestRate FIVE_PERCENT =
            new InterestRate(BigDecimal.valueOf(0.05));
}
