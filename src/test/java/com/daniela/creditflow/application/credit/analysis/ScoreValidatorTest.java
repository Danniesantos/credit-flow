package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ScoreValidatorTest {
    private ScoreValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ScoreValidator();
    }

    @Test
    @DisplayName("Should approve credit when customer has good score")
    void shouldApproveWhenScoreIsGood() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        Customer customer =
                CustomerTestFactory.customer();


        AnalysisResult result =
                validator.handle(
                        credit,
                        customer
                );


        assertThat(result.approved())
                .isTrue();
    }

    @Test
    @DisplayName("Should reject credit when customer score is below minimum")
    void shouldRejectWhenScoreIsBelowMinimum() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        Customer customer =
                CustomerTestFactory.customerWithBadScore();


        AnalysisResult result =
                validator.handle(
                        credit,
                        customer
                );


        assertThat(result.approved())
                .isFalse();

        assertThat(result.reason())
                .isEqualTo(
                        "Credit score below minimum required"
                );
    }
}