package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.CreditDetailsOutput;
import com.daniela.creditflow.application.credit.mapper.CreditOutputMapper;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.repository.CreditRepository;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindCreditUseCase {

    private final CreditRepository repository;
    private final CreditOutputMapper creditMapper;

    public FindCreditUseCase(CreditRepository repository,
                             CreditOutputMapper creditMapper) {

        this.repository = repository;
        this.creditMapper = creditMapper;
    }

    public CreditDetailsOutput execute(UUID id) {

        CreditId creditId = new CreditId(id);
        Credit credit = repository
                .findById(creditId)
                .orElseThrow(() -> new CreditNotFoundException(creditId));

        return creditMapper.toDetailsOutput(credit);
    }
}
