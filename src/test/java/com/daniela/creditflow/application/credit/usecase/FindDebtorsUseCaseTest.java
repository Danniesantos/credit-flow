package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.DebtorOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindDebtorsUseCaseTest {

    private static final Instant NOW =
            Instant.parse("2026-08-24T15:00:00Z");

    private static final LocalDate TODAY =
            LocalDate.of(2026, 8, 24);

    @Mock
    private CreditRepository repository;

    @Mock
    private CreditApplicationMapper creditMapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private FindDebtorsUseCase useCase;

    @BeforeEach
    void setup() {

        when(clock.instant())
                .thenReturn(NOW);

        when(clock.getZone())
                .thenReturn(ZoneId.of("America/Sao_Paulo"));
    }

    @Test
    @DisplayName("Should return debtors page")
    void shouldReturnDebtorsPage() {

        Pageable pageable =
                PageRequest.of(0, 10);

        Credit credit =
                CreditTestFactory.creditWithOverdueInstallments();

        Page<Credit> credits =
                new PageImpl<>(
                        List.of(credit)
                );

        DebtorOutput debtor =
                mock(DebtorOutput.class);

        when(repository.findCreditsWithOverdueInstallments(pageable))
                .thenReturn(credits);

        when(creditMapper.toDebtorOutput(
                credit,
                TODAY
        ))
                .thenReturn(debtor);

        Page<DebtorOutput> result =
                useCase.execute(pageable);

        assertThat(result.getContent())
                .containsExactly(debtor);

        assertThat(result.getTotalElements())
                .isEqualTo(1);

        verify(repository)
                .findCreditsWithOverdueInstallments(pageable);

        verify(creditMapper)
                .toDebtorOutput(
                        credit,
                        TODAY
                );
    }

    @Test
    @DisplayName("Should return empty page when there are no debtors")
    void shouldReturnEmptyPageWhenNoDebtors() {

        Pageable pageable =
                PageRequest.of(0, 10);

        when(repository.findCreditsWithOverdueInstallments(pageable))
                .thenReturn(Page.empty());

        Page<DebtorOutput> result =
                useCase.execute(pageable);

        assertThat(result)
                .isEmpty();

        verifyNoInteractions(creditMapper);
    }
}