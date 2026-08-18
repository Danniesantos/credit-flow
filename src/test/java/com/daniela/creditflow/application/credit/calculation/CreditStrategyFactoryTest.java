package com.daniela.creditflow.application.credit.calculation;

import com.daniela.creditflow.application.credit.calculation.strategy.BusinessCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.CreditInterestCalculationStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PayrollCreditStrategy;
import com.daniela.creditflow.application.credit.calculation.strategy.PersonalCreditStrategy;
import com.daniela.creditflow.domain.model.CreditType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CreditStrategyFactoryTest {

    private PersonalCreditStrategy personal;
    private PayrollCreditStrategy payroll;
    private BusinessCreditStrategy business;

    private CreditStrategyFactory factory;

    @BeforeEach
    void setUp() {

        personal = mock(PersonalCreditStrategy.class);
        payroll = mock(PayrollCreditStrategy.class);
        business = mock(BusinessCreditStrategy.class);

        factory = new CreditStrategyFactory(
                personal,
                payroll,
                business
        );
    }

    @Test
    @DisplayName("Should return personal credit strategy")
    void shouldReturnPersonalCreditStrategy() {

        CreditInterestCalculationStrategy strategy =
                factory.getStrategy(CreditType.PERSONAL);

        assertThat(strategy)
                .isEqualTo(personal);
    }

    @Test
    @DisplayName("Should return payroll credit strategy")
    void shouldReturnPayrollCreditStrategy() {

        CreditInterestCalculationStrategy strategy =
                factory.getStrategy(CreditType.PAYROLL);

        assertThat(strategy)
                .isEqualTo(payroll);
    }

    @Test
    @DisplayName("Should return business credit strategy")
    void shouldReturnBusinessCreditStrategy() {

        CreditInterestCalculationStrategy strategy =
                factory.getStrategy(CreditType.BUSINESS);

        assertThat(strategy)
                .isEqualTo(business);
    }

    @Test
    @DisplayName("Should throw exception when strategy type is null")
    void shouldThrowExceptionWhenStrategyTypeIsNull() {

        assertThatThrownBy(() ->
                factory.getStrategy(null)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Credit type cannot be null");
    }
}