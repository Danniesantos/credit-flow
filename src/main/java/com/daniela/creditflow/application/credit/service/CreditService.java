package com.daniela.creditflow.application.credit.service;

import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.repository.CreditRepository;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public Credit findCredit(CreditId id) {
        return creditRepository.findByIdWithInstallments(id)
                .orElseThrow(() ->
                        new CreditNotFoundException(id));
    }
}
