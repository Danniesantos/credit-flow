package com.daniela.creditflow.application.credit.service;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.calculation.CreditStrategyFactory;
import com.daniela.creditflow.application.credit.calculation.strategy.CreditInterestCalculationStrategy;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.valueobject.Money;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCalculationServiceTest {

    @Mock
    private CreditStrategyFactory strategyFactory;

    @Mock
    private CreditInterestCalculationStrategy strategy;

    private CreditCalculationService service;

    @BeforeEach
    void setUp() {
        service = new CreditCalculationService(strategyFactory);
    }

    @Test
    @DisplayName("Should calculate credit using strategy")
    void shouldCalculateCreditUsingStrategy() {

        Money requestedAmount =
                TestConstants.TOTAL_CREDIT_AMOUNT;

        Integer installments = 12;

        CreditCalculationResult expected =
                mock(CreditCalculationResult.class);

        when(strategyFactory.getStrategy(CreditType.PERSONAL))
                .thenReturn(strategy);

        when(strategy.calculate(
                requestedAmount,
                installments))
                .thenReturn(expected);


        CreditCalculationResult result =
                service.calculate(
                        CreditType.PERSONAL,
                        requestedAmount,
                        installments
                );


        assertThat(result)
                .isEqualTo(expected);

        verify(strategyFactory)
                .getStrategy(CreditType.PERSONAL);

        verify(strategy)
                .calculate(
                        requestedAmount,
                        installments
                );
    }
}