package com.daniela.creditflow.application.credit.service;

import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditService service;

    @Test
    @DisplayName("Should return credit when found")
    void shouldReturnCreditWhenFound() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        when(creditRepository.findByIdWithInstallments(
                credit.getId()
        )).thenReturn(Optional.of(credit));

        Credit result =
                service.findCredit(
                        credit.getId()
                );

        assertThat(result)
                .isEqualTo(credit);

        verify(creditRepository)
                .findByIdWithInstallments(
                        credit.getId()
                );
    }

    @Test
    @DisplayName("Should throw exception when credit is not found")
    void shouldThrowExceptionWhenCreditIsNotFound() {

        CreditId id =
                new CreditId();

        when(creditRepository.findByIdWithInstallments(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.findCredit(id)
        )
                .isInstanceOf(CreditNotFoundException.class);

        verify(creditRepository)
                .findByIdWithInstallments(id);
    }

}