package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.output.RequestCreditOutput;
import com.daniela.creditflow.application.credit.factory.CreditFactory;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestCreditUseCase {

    private final CreditFactory creditFactory;
    private final CreditRepository creditRepository;
    private final CreditApplicationMapper creditOutputMapper;
    private final CustomerService customerService;

    @Transactional
    public RequestCreditOutput execute(RequestCreditInput input) {

        CustomerId customerId =
                new CustomerId(input.customerId());

        customerService.findCustomer(customerId);

        Credit credit =
                creditFactory.create(input);

        Credit saved =
                creditRepository.save(credit);

        return creditOutputMapper.toCreditOutput(saved);
    }

}
