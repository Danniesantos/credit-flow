package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.valueobject.InterestRate;
import com.daniela.creditflow.domain.valueobject.Money;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class TestConstants {

    private TestConstants() {
    }

    public static final Money TOTAL_CREDIT_AMOUNT =
            new Money(BigDecimal.valueOf(10_000));

    public static final Money INSTALLMENT_AMOUNT =
            new Money(BigDecimal.valueOf(1_000));

    public static final InterestRate FIVE_PERCENT =
            new InterestRate(BigDecimal.valueOf(0.05));

    public static final Integer INSTALLMENT_NUMBER = 1;

    public static final LocalDate TEST_DATE =
            LocalDate.of(2026, 8, 24);

    public static final Instant PAID_AT =
            Instant.parse("2026-08-07T12:00:00Z");

    public static final String CUSTOMER_NAME = "Testando";

    public static final LocalDate CUSTOMER_BIRTH_DATE =
            LocalDate.of(1992, 1, 10);

    public static final Money CUSTOMER_MONTHLY_INCOME =
            new Money(BigDecimal.valueOf(5000));

    public static final Instant CREATED_AT =
            Instant.parse("2026-08-07T12:00:00Z");

    public static final ZoneId ZONE_ID =
            ZoneId.of("America/Sao_Paulo");

    public static final Instant NOW =
            Instant.parse("2026-08-24T15:00:00Z");

    public static final LocalDate TODAY =
            LocalDate.of(2026, 8, 24);

    public static final Clock FIXED_CLOCK =
            Clock.fixed(NOW, ZONE_ID);

}
