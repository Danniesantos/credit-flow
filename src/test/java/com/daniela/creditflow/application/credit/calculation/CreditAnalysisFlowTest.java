package com.daniela.creditflow.application.credit.calculation;

import com.daniela.creditflow.application.credit.analysis.CreditAnalysisChain;
import com.daniela.creditflow.application.credit.analysis.IncomeValidator;
import com.daniela.creditflow.application.credit.analysis.LimitValidator;
import com.daniela.creditflow.application.credit.analysis.ScoreValidator;
import com.daniela.creditflow.application.credit.calculation.strategy.BusinessCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PayrollCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PersonalCreditStrategy;
import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.valueobject.Money;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreditAnalysisFlowTest {

    private CreditCalculationService calculationService;
    private CreditAnalysisChain analysisChain;

    @BeforeEach
    void setUp() {

        CreditStrategyFactory strategyFactory =
                new CreditStrategyFactory(
                        new PersonalCreditStrategy(),
                        new PayrollCreditStrategy(),
                        new BusinessCreditStrategy()
                );

        calculationService =
                new CreditCalculationService(
                        strategyFactory
                );


        analysisChain =
                new CreditAnalysisChain(
                        new ScoreValidator(),
                        new IncomeValidator(),
                        new LimitValidator()
                );
    }

    @Test
    @DisplayName("Should calculate and approve credit request")
    void shouldCalculateAndApproveCreditRequest() {

        Money requestedAmount =
                new Money(new BigDecimal("10000"));

        CreditCalculationResult calculation =
                calculationService.calculate(
                        CreditType.PERSONAL,
                        requestedAmount,
                        12
                );

        assertThat(calculation.totalAmount().value())
                .isGreaterThan(requestedAmount.value());


        Credit credit =
                CreditTestFactory.creditWithAmount(
                        requestedAmount
                );


        AnalysisResult result =
                analysisChain.chain()
                        .handle(
                                credit,
                                CustomerTestFactory.customer()
                        );


        assertThat(result.approved())
                .isTrue();
    }
}
