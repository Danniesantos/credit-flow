package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.OverdueOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCreditOverdueUseCaseTest {

    private static final Clock FIXED_CLOCK =
            Clock.fixed(
                    Instant.parse("2026-08-24T15:00:00Z"),
                    ZoneId.of("America/Sao_Paulo")
            );

    private static final LocalDate TODAY =
            LocalDate.of(2026, 8, 24);

    @Mock
    private CreditService creditService;

    @Mock
    private CreditApplicationMapper creditMapper;

    private FindCreditOverdueUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new FindCreditOverdueUseCase(
                creditService,
                creditMapper,
                FIXED_CLOCK
        );
    }

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

        when(creditMapper.toOverdueOutput(
                credit,
                TODAY
        ))
                .thenReturn(expected);

        OverdueOutput result =
                useCase.execute(creditId);

        assertThat(result)
                .isEqualTo(expected);

        verify(creditService)
                .findCredit(creditId);

        verify(creditMapper)
                .toOverdueOutput(
                        credit,
                        TODAY
                );
    }
}