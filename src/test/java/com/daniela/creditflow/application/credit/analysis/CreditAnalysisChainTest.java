package com.daniela.creditflow.application.credit.analysis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class CreditAnalysisChainTest {

    @Test
    @DisplayName("Should create credit analysis chain in correct order")
    void shouldCreateCreditAnalysisChainInCorrectOrder() {

        ScoreValidator scoreValidator =
                mock(ScoreValidator.class);

        IncomeValidator incomeValidator =
                mock(IncomeValidator.class);

        LimitValidator limitValidator =
                mock(LimitValidator.class);


        when(scoreValidator.next(incomeValidator))
                .thenReturn(incomeValidator);

        CreditAnalysisChain chain =
                new CreditAnalysisChain(
                        scoreValidator,
                        incomeValidator,
                        limitValidator
                );

        assertThat(chain.chain())
                .isEqualTo(scoreValidator);

        verify(scoreValidator)
                .next(incomeValidator);

        verify(incomeValidator)
                .next(limitValidator);
    }
}