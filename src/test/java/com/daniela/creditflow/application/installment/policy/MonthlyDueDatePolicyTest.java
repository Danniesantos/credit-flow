package com.daniela.creditflow.application.installment.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MonthlyDueDatePolicyTest {

    private final MonthlyDueDatePolicy policy =
            new MonthlyDueDatePolicy();

    @Test
    @DisplayName("Should calculate due date by adding months")
    void shouldCalculateDueDateByAddingMonths() {

        LocalDate referenceDate =
                LocalDate.of(2026, 1, 15);

        LocalDate result =
                policy.calculate(3, referenceDate);

        assertThat(result)
                .isEqualTo(
                        LocalDate.of(2026, 4, 15)
                );
    }

    @Test
    @DisplayName("Should return reference date for first installment")
    void shouldReturnReferenceDateForFirstInstallment() {

        LocalDate referenceDate =
                LocalDate.of(2026, 1, 15);

        LocalDate result =
                policy.calculate(0, referenceDate);

        assertThat(result)
                .isEqualTo(referenceDate);
    }
}