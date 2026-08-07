package com.daniela.creditflow.application.credit.dto.output;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AnalysisResultTest {

    @Test
    @DisplayName("Should create successful analysis result")
    void shouldCreateSuccessResult() {

        AnalysisResult result =
                AnalysisResult.success();

        assertThat(result.approved())
                .isTrue();

        assertThat(result.reason())
                .isNull();
    }

    @Test
    @DisplayName("Should create failed analysis result")
    void shouldCreateFailureResult() {

        AnalysisResult result =
                AnalysisResult.failure(
                        "Credit score below minimum required"
                );

        assertThat(result.approved())
                .isFalse();

        assertThat(result.reason())
                .isEqualTo(
                        "Credit score below minimum required"
                );
    }

    @Test
    @DisplayName("Should not create approved result with reason")
    void shouldNotCreateApprovedResultWithReason() {

        assertThatThrownBy(() ->
                new AnalysisResult(
                        true,
                        "Some reason"
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Approved analysis cannot contain a reason"
                );
    }

    @Test
    @DisplayName("Should not create failed result without reason")
    void shouldNotCreateFailedResultWithoutReason() {

        assertThatThrownBy(() ->
                new AnalysisResult(
                        false,
                        null
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Rejected analysis must contain a reason"
                );
    }

    @Test
    @DisplayName("Should not create failed result with blank reason")
    void shouldNotCreateFailedResultWithBlankReason() {

        assertThatThrownBy(() ->
                new AnalysisResult(
                        false,
                        " "
                )
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

}