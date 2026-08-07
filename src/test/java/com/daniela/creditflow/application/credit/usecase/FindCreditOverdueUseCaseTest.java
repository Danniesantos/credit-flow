package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.OverdueOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCreditOverdueUseCaseTest {

    @Mock
    private CreditService creditService;

    @Mock
    private CreditApplicationMapper creditMapper;

    @InjectMocks
    private FindCreditOverdueUseCase useCase;

    @Test
    @DisplayName("Should return overdue credit information")
    void shouldReturnOverdueCreditInformation() {

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        CreditId creditId =
                credit.getId();


        OverdueOutput expected =
                new OverdueOutput(
                        true,
                        2L,
                        new BigDecimal("2000"),
                        List.of()
                );

        when(creditService.findCredit(creditId))
                .thenReturn(credit);

        when(creditMapper.toOverdueOutput(credit))
                .thenReturn(expected);

        OverdueOutput result =
                useCase.execute(creditId);

        assertThat(result)
                .isEqualTo(expected);

        verify(creditService)
                .findCredit(creditId);

        verify(creditMapper)
                .toOverdueOutput(credit);
    }
}