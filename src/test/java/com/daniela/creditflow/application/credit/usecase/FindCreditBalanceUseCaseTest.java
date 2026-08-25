package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.BalanceOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCreditBalanceUseCaseTest {

    @Mock
    private CreditService creditService;

    @Mock
    private CreditApplicationMapper creditMapper;

    @InjectMocks
    private FindCreditBalanceUseCase useCase;

    @Test
    @DisplayName("Should return credit balance")
    void shouldReturnCreditBalance() {

        Credit credit =
                CreditTestFactory.creditWithOnePaidInstallment();

        CreditId creditId =
                credit.getId();

        BalanceOutput expected =
                new BalanceOutput(
                        new BigDecimal("1000"),
                        new BigDecimal("500"),
                        new BigDecimal("500"),
                        1
                );

        when(creditService.findCredit(creditId))
                .thenReturn(credit);

        when(creditMapper.toBalanceOutput(credit))
                .thenReturn(expected);

        BalanceOutput result =
                useCase.execute(creditId);

        assertThat(result)
                .isEqualTo(expected);

        verify(creditService)
                .findCredit(creditId);

        verify(creditMapper)
                .toBalanceOutput(credit);
    }

    @Test
    @DisplayName("Should find credit before mapping balance")
    void shouldFindCreditBeforeMappingBalance() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        when(creditService.findCredit(
                credit.getId()
        )).thenReturn(credit);

        BalanceOutput output =
                mock(BalanceOutput.class);

        when(creditMapper.toBalanceOutput(credit))
                .thenReturn(output);

        useCase.execute(
                credit.getId()
        );

        InOrder order =
                inOrder(
                        creditService,
                        creditMapper
                );

        order.verify(creditService)
                .findCredit(
                        credit.getId()
                );

        order.verify(creditMapper)
                .toBalanceOutput(
                        credit
                );
    }
}