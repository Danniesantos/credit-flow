package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.event.CreditCanceledEvent;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelCreditUseCaseTest {

    @Mock
    private CreditService creditService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CancelCreditUseCase useCase;

    @Test
    @DisplayName("Should cancel credit and publish canceled event")
    void shouldCancelCredit() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        CreditId creditId =
                credit.getId();


        when(creditService.findCredit(creditId))
                .thenReturn(credit);


        useCase.execute(creditId);


        assertThat(credit.getStatus())
                .isEqualTo(
                        CreditStatus.CANCELED
                );


        verify(creditRepository)
                .save(credit);


        verify(eventPublisher)
                .publishEvent(
                        any(CreditCanceledEvent.class)
                );
    }

    @Test
    @DisplayName("Should find credit before canceling")
    void shouldFindCreditBeforeCanceling() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();


        when(creditService.findCredit(
                credit.getId()
        )).thenReturn(credit);


        useCase.execute(
                credit.getId()
        );


        verify(creditService)
                .findCredit(
                        credit.getId()
                );
    }

    @Test
    @DisplayName("Should not save when cancellation fails")
    void shouldNotSaveWhenCancellationFails() {

        Credit credit =
                CreditTestFactory.canceledCredit();


        when(creditService.findCredit(
                credit.getId()
        )).thenReturn(credit);


        assertThatThrownBy(() ->
                useCase.execute(
                        credit.getId()
                )
        )
                .isInstanceOf(
                        InvalidDomainStateException.class
                );


        verify(creditRepository, never())
                .save(any());

        verify(eventPublisher, never())
                .publishEvent(any());
    }
}