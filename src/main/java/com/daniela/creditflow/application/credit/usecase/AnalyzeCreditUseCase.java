package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.analysis.CreditAnalysisChain;
import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.application.credit.dto.output.AnalyzeCreditOutput;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.event.CreditRejectedEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AnalyzeCreditUseCase {

    private final CreditRepository creditRepository;
    private final CreditService creditService;
    private final CustomerService customerService;
    private final CreditAnalysisChain creditAnalysisChain;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public AnalyzeCreditOutput execute(CreditId creditId) {

        Credit credit =
                creditService.findCredit(creditId);

        Customer customer =
                customerService
                        .findCustomer(credit.getCustomerId());

        AnalysisResult result =
                creditAnalysisChain.chain()
                        .handle(credit, customer);

        Instant now =
                clock.instant();

        if (result.approved()) {
            credit.approve(now);
        } else {
            credit.reject(now);
        }

        creditRepository.save(credit);

        publishAnalysisEvent(
                credit,
                result,
                now
        );

        return new AnalyzeCreditOutput(
                credit.getId().value(),
                credit.getStatus(),
                result.reason()
        );
    }

    private void publishAnalysisEvent(
            Credit credit,
            AnalysisResult result,
            Instant now) {

        if (result.approved()) {

            eventPublisher.publishEvent(
                    new CreditApprovedEvent(
                            credit.getId(),
                            credit.getCustomerId(),
                            now
                    ));

        } else {

            eventPublisher.publishEvent(
                    new CreditRejectedEvent(
                            credit.getId(),
                            credit.getCustomerId(),
                            now
                    ));
        }
    }
}
