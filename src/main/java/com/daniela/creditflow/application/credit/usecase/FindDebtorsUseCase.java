package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.DebtorOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.domain.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FindDebtorsUseCase {

    private final CreditRepository repository;
    private final CreditApplicationMapper creditMapper;
    private final Clock clock;

    public Page<DebtorOutput> execute(Pageable pageable) {

        LocalDate today = LocalDate.now(clock);

        return repository
                .findCreditsWithOverdueInstallments(pageable)
                .map(credit ->
                        creditMapper.toDebtorOutput(
                                credit,
                                today
                        )
                );
    }
}
