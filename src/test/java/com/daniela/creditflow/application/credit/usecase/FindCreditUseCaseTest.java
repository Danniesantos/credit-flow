package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.CreditDetailsOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCreditUseCaseTest {

    @Mock
    private CreditService creditService;

    @Mock
    private CreditApplicationMapper creditMapper;

    @InjectMocks
    private FindCreditUseCase useCase;

    @Test
    @DisplayName("Should return credit details")
    void shouldReturnCreditDetails() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        CreditId creditId =
                credit.getId();

        CreditDetailsOutput expected =
                mock(CreditDetailsOutput.class);

        when(creditService.findCredit(creditId))
                .thenReturn(credit);

        when(creditMapper.toDetailsOutput(credit))
                .thenReturn(expected);

        CreditDetailsOutput result =
                useCase.execute(creditId);

        assertThat(result)
                .isEqualTo(expected);

        verify(creditService)
                .findCredit(creditId);

        verify(creditMapper)
                .toDetailsOutput(credit);
    }
}