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

public class CreditAnalysisChainIntegrationTest {


    private CreditAnalysisChain chain;

    @BeforeEach
    void setUp() {

        chain = new CreditAnalysisChain(
                new ScoreValidator(),
                new IncomeValidator(),
                new LimitValidator()
        );
    }


    @Test
    @DisplayName("Should approve credit when all validators pass")
    void shouldApproveWhenAllValidatorsPass() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        Customer customer =
                CustomerTestFactory.customer();


        AnalysisResult result =
                chain.chain()
                        .handle(
                                credit,
                                customer
                        );


        assertThat(result.approved())
                .isTrue();
    }

    @Test
    @DisplayName("Should reject credit when score validation fails")
    void shouldRejectWhenScoreValidationFails() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        Customer customer =
                CustomerTestFactory.customerWithBadScore();


        AnalysisResult result =
                chain.chain()
                        .handle(
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
