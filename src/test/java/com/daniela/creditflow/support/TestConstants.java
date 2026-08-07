package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.valueObject.InterestRate;
import com.daniela.creditflow.domain.valueObject.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class TestConstants {

    private TestConstants() {
    }

    public static final Money TEN_THOUSAND =
            new Money(BigDecimal.valueOf(10_000));

    public static final Money ONE_THOUSAND =
            new Money(BigDecimal.valueOf(1_000));

    public static final InterestRate FIVE_PERCENT =
            new InterestRate(BigDecimal.valueOf(0.05));

    public static final Money INSTALLMENT_AMOUNT =
            new Money(BigDecimal.valueOf(1_000));

    public static final Integer INSTALLMENT_NUMBER = 1;

    public static final LocalDate TEST_DATE =
            LocalDate.of(2026, 8, 7);

    public static final Instant PAID_AT =
            Instant.parse("2026-08-07T12:00:00Z");
}
