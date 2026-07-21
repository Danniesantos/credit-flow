package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.analysis.CreditAnalysisChain;
import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.application.credit.dto.output.AnalyzeCreditOutput;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.credit.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.credit.event.CreditRejectedEvent;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.repository.CreditRepository;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.model.Customer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class AnalyzeCreditUseCase {

    private final CreditRepository creditRepository;
    private final CreditService creditService;
    private final CustomerService customerService;
    private final CreditAnalysisChain creditAnalysisChain;
    private final ApplicationEventPublisher eventPublisher;

    public AnalyzeCreditUseCase(CreditRepository creditRepository,
                                CreditService creditService,
                                CustomerService customerService,
                                CreditAnalysisChain creditAnalysisChain,
                                ApplicationEventPublisher eventPublisher) {

        this.creditRepository = creditRepository;
        this.creditService = creditService;
        this.customerService = customerService;
        this.creditAnalysisChain = creditAnalysisChain;
        this.eventPublisher = eventPublisher;
    }

    public AnalyzeCreditOutput execute(CreditId creditId) {

        Credit credit =
                creditService.findCredit(creditId);

        Customer customer =
                customerService
                        .findCustomer(credit.getCustomerId());

        AnalysisResult result =
                creditAnalysisChain.chain().handle(credit, customer);

        if (result.approved()) {
            credit.approve();
        } else {
            credit.reject();
        }

        creditRepository.save(credit);

        publishAnalysisEvent(credit, result);

        return new AnalyzeCreditOutput(
                credit.getId().value(),
                credit.getStatus(),
                result.reason()
        );
    }

    private void publishAnalysisEvent(
            Credit credit,
            AnalysisResult result) {

        if (result.approved()) {

            eventPublisher.publishEvent(
                    new CreditApprovedEvent(
                            credit.getId(),
                            credit.getCustomerId(),
                            Instant.now()));

        } else {

            eventPublisher.publishEvent(
                    new CreditRejectedEvent(
                            credit.getId(),
                            credit.getCustomerId(),
                            Instant.now()));
        }
    }
}
