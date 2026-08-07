package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.output.RequestCreditOutput;
import com.daniela.creditflow.application.credit.factory.CreditFactory;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestCreditUseCaseTest {

    @Mock
    private CreditFactory creditFactory;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditApplicationMapper creditOutputMapper;

    @InjectMocks
    private RequestCreditUseCase useCase;

    @Test
    @DisplayName("Should create credit request")
    void shouldCreateCreditRequest() {

        RequestCreditInput input =
                new RequestCreditInput(
                        UUID.randomUUID(),
                        new BigDecimal("10000"),
                        12,
                        CreditType.PERSONAL
                );

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        RequestCreditOutput expected =
                mock(RequestCreditOutput.class);

        when(creditFactory.create(input))
                .thenReturn(credit);

        when(creditRepository.save(credit))
                .thenReturn(credit);

        when(creditOutputMapper.toCreditOutput(credit))
                .thenReturn(expected);

        RequestCreditOutput result =
                useCase.execute(input);

        assertThat(result)
                .isEqualTo(expected);

        verify(creditFactory)
                .create(input);

        verify(creditRepository)
                .save(credit);

        verify(creditOutputMapper)
                .toCreditOutput(credit);
    }

    @Test
    @DisplayName("Should save credit created by factory")
    void shouldSaveCreatedCredit() {

        RequestCreditInput input =
                mock(RequestCreditInput.class);

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        when(creditFactory.create(input))
                .thenReturn(credit);

        when(creditRepository.save(credit))
                .thenReturn(credit);

        when(creditOutputMapper.toCreditOutput(credit))
                .thenReturn(
                        mock(RequestCreditOutput.class)
                );

        useCase.execute(input);

        verify(creditRepository)
                .save(
                        same(credit)
                );
    }
}