package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.DebtorOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.domain.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindDebtorsUseCase {

    private final CreditRepository repository;
    private final CreditApplicationMapper creditMapper;

    public Page<DebtorOutput> execute(Pageable pageable) {

        return repository
                .findCreditsWithOverdueInstallments(pageable)
                .map(creditMapper::toDebtorOutput);
    }
}
