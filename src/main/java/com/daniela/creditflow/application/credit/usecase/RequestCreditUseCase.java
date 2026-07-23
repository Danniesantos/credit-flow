package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.output.RequestCreditOutput;
import com.daniela.creditflow.application.credit.factory.CreditFactory;
import com.daniela.creditflow.application.credit.mapper.CreditOutputMapper;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestCreditUseCase {

    private final CreditFactory creditFactory;
    private final CreditRepository creditRepository;
    private final CreditOutputMapper creditOutputMapper;

    public RequestCreditUseCase(CreditFactory creditFactory,
                                CreditRepository creditRepository,
                                CreditOutputMapper creditOutputMapper) {

        this.creditFactory = creditFactory;
        this.creditRepository = creditRepository;
        this.creditOutputMapper = creditOutputMapper;
    }

    public RequestCreditOutput execute(RequestCreditInput input) {

        Credit credit =
                creditFactory.create(input);

        Credit saved =
                creditRepository.save(credit);

        return creditOutputMapper.toCreditOutput(saved);
    }

}
