package com.daniela.creditflow.domain.valueobject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CreditScoreTest {

    @Test
    @DisplayName("Should reject null credit score")
    void shouldRejectNullCreditScore() {

        assertThatThrownBy(() ->
                new CreditScore(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Credit score cannot be null");
    }

    @Test
    @DisplayName("Should reject credit score below zero")
    void shouldRejectCreditScoreBelowZero() {

        assertThatThrownBy(() ->
                new CreditScore(-1)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage(
                        "Credit score must be between 0 and 1000"
                );
    }

    @Test
    @DisplayName("Should reject credit score above 1000")
    void shouldRejectCreditScoreAbove1000() {

        assertThatThrownBy(() ->
                new CreditScore(1001)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage(
                        "Credit score must be between 0 and 1000"
                );
    }

    @Test
    @DisplayName("Should classify excellent credit score")
    void shouldClassifyExcellentCreditScore() {

        CreditScore score =
                new CreditScore(850);

        assertThat(score.isExcellent())
                .isTrue();

        assertThat(score.isGood())
                .isFalse();

        assertThat(score.isAverage())
                .isFalse();

        assertThat(score.isPoor())
                .isFalse();
    }

    @Test
    @DisplayName("Should classify good credit score")
    void shouldClassifyGoodCreditScore() {

        CreditScore score =
                new CreditScore(700);

        assertThat(score.isGood())
                .isTrue();

        assertThat(score.isExcellent())
                .isFalse();
    }

    @Test
    @DisplayName("Should classify average credit score")
    void shouldClassifyAverageCreditScore() {

        CreditScore score =
                new CreditScore(500);

        assertThat(score.isAverage())
                .isTrue();

        assertThat(score.isGood())
                .isFalse();

        assertThat(score.isPoor())
                .isFalse();
    }

    @Test
    @DisplayName("Should classify poor credit score")
    void shouldClassifyPoorCreditScore() {

        CreditScore score =
                new CreditScore(499);

        assertThat(score.isPoor())
                .isTrue();

        assertThat(score.isAverage())
                .isFalse();
    }
}