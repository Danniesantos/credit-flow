package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.SimulateCreditOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulateCreditUseCaseTest {

    @Mock
    private CreditCalculationService calculationService;

    @Mock
    private CreditApplicationMapper creditMapper;

    @InjectMocks
    private SimulateCreditUseCase useCase;

    @Test
    @DisplayName("Should simulate credit")
    void shouldSimulateCredit() {

        SimulateCreditInput input =
                new SimulateCreditInput(
                        new BigDecimal("10000"),
                        12,
                        CreditType.PERSONAL
                );

        CreditCalculationResult calculation =
                mock(CreditCalculationResult.class);

        Money installmentAmount =
                new Money(
                        new BigDecimal("900")
                );

        SimulateCreditOutput expected =
                mock(SimulateCreditOutput.class);

        when(calculationService.calculate(
                CreditType.PERSONAL,
                new Money(new BigDecimal("10000")),
                12
        )).thenReturn(calculation);

        when(calculation.installmentAmount(12))
                .thenReturn(installmentAmount);

        when(creditMapper.toSimulateOutput(
                new Money(new BigDecimal("10000")),
                calculation,
                12,
                installmentAmount
        )).thenReturn(expected);

        SimulateCreditOutput result =
                useCase.execute(input);

        assertThat(result)
                .isEqualTo(expected);

        verify(calculationService)
                .calculate(
                        CreditType.PERSONAL,
                        new Money(
                                new BigDecimal("10000")
                        ),
                        12
                );

        verify(calculation)
                .installmentAmount(12);

        verify(creditMapper)
                .toSimulateOutput(
                        new Money(new BigDecimal("10000")),
                        calculation,
                        12,
                        installmentAmount
                );
    }
}