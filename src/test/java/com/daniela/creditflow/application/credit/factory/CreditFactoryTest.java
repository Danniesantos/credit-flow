package com.daniela.creditflow.application.credit.factory;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.valueObject.Money;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CreditFactoryTest {

    @Test
    @DisplayName("Should create credit from request input")
    void shouldCreateCreditFromRequestInput() {

        CreditCalculationService calculationService =
                mock(CreditCalculationService.class);

        when(calculationService.calculate(
                any(),
                any(),
                any()
        )).thenReturn(
                new CreditCalculationResult(
                        Money.zero(),
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        TestConstants.FIVE_PERCENT
                )
        );


        CreditFactory factory =
                new CreditFactory(calculationService);


        RequestCreditInput input =
                new RequestCreditInput(
                        UUID.randomUUID(),
                        new BigDecimal("10000"),
                        12,
                        CreditType.PERSONAL
                );


        Credit credit =
                factory.create(input);


        assertThat(credit.getRequestedAmount())
                .isEqualTo(
                        new Money(new BigDecimal("10000"))
                );

        assertThat(credit.getCreditType())
                .isEqualTo(CreditType.PERSONAL);

        assertThat(credit.getInstallmentsQuantity())
                .isEqualTo(12);

        assertThat(credit.getStatus())
                .isEqualTo(CreditStatus.UNDER_ANALYSIS);
    }

    @Test
    @DisplayName("Should calculate credit before creation")
    void shouldCalculateCreditBeforeCreation() {

        CreditCalculationService calculationService =
                mock(CreditCalculationService.class);


        when(calculationService.calculate(
                any(),
                any(),
                any()
        )).thenReturn(
                new CreditCalculationResult(
                        Money.zero(),
                        TestConstants.TOTAL_CREDIT_AMOUNT,
                        TestConstants.FIVE_PERCENT
                )
        );


        CreditFactory factory =
                new CreditFactory(calculationService);


        RequestCreditInput input =
                new RequestCreditInput(
                        UUID.randomUUID(),
                        new BigDecimal("10000"),
                        12,
                        CreditType.PERSONAL
                );


        factory.create(input);


        verify(calculationService)
                .calculate(
                        eq(CreditType.PERSONAL),
                        any(Money.class),
                        eq(12)
                );
    }
}