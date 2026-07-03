package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.analysis.CreditAnalysisChain;
import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.application.credit.dto.output.AnalyzeCreditOutput;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.repository.CreditRepository;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.exception.CustomerNotFoundException;
import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.customer.repository.CustomerRepository;
import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnalyzeCreditUseCase {

    private final CreditRepository creditRepository;
    private final CustomerRepository customerRepository;
    private final CreditAnalysisChain creditAnalysisChain;

    public AnalyzeCreditUseCase(CreditRepository creditRepository,
                                CustomerRepository customerRepository,
                                CreditAnalysisChain creditAnalysisChain) {

        this.creditRepository = creditRepository;
        this.customerRepository = customerRepository;
        this.creditAnalysisChain = creditAnalysisChain;
    }

    public AnalyzeCreditOutput execute(UUID id) {

        CreditId creditId = new CreditId(id);
        Credit credit =
                creditRepository.findById(creditId)
                        .orElseThrow(() -> new CreditNotFoundException(creditId));

        Customer customer =
                customerRepository.findById(credit.getCustomerId())
                        .orElseThrow(() -> new CustomerNotFoundException(
                                credit.getCustomerId()
                        ));

        AnalysisResult result =
                creditAnalysisChain.chain().handle(credit, customer);

        if (result.approved()) {
            credit.approve();
        } else {
            credit.reject();
        }

        creditRepository.save(credit);

        return new AnalyzeCreditOutput(
                credit.getId().value(),
                credit.getStatus(),
                result.reason()
        );
    }
}
