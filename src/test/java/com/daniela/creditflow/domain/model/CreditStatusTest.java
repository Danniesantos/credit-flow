package com.daniela.creditflow.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CreditStatusTest {

    @Test
    @DisplayName("Should return open credit statuses")
    void shouldReturnOpenCreditStatuses() {

        Set<CreditStatus> result =
                CreditStatus.openStatuses();

        assertThat(result)
                .containsExactlyInAnyOrder(
                        CreditStatus.UNDER_ANALYSIS,
                        CreditStatus.APPROVED,
                        CreditStatus.CONTRACTED
                );
    }

    @Test
    @DisplayName("Should not contain closed credit statuses")
    void shouldNotContainClosedCreditStatuses() {

        Set<CreditStatus> result =
                CreditStatus.openStatuses();

        assertThat(result)
                .doesNotContain(
                        CreditStatus.REJECTED,
                        CreditStatus.CANCELED,
                        CreditStatus.PAID_OFF
                );
    }
}