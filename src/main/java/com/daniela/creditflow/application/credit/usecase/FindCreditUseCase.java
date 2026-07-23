package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.CreditDetailsOutput;
import com.daniela.creditflow.application.credit.mapper.CreditOutputMapper;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueObject.CreditId;
import org.springframework.stereotype.Service;

@Service
public class FindCreditUseCase {

    private final CreditService creditService;
    private final CreditOutputMapper creditMapper;

    public FindCreditUseCase(CreditService creditService,
                             CreditOutputMapper creditMapper) {

        this.creditService = creditService;
        this.creditMapper = creditMapper;
    }

    public CreditDetailsOutput execute(CreditId creditId) {

        Credit credit =
                creditService
                        .findCredit(creditId);

        return creditMapper.toDetailsOutput(credit);
    }
}
