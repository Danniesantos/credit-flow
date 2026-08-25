package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.installment.factory.InstallmentFactory;
import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.event.CreditContractedEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractCreditUseCase {

    private final CreditRepository creditRepository;
    private final CreditService creditService;
    private final InstallmentFactory installmentFactory;
    private final CreditCalculationService calculationService;
    private final DueDatePolicy dueDatePolicy;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public void execute(CreditId creditId) {

        Credit credit =
                creditService.findCredit(creditId);

        CreditCalculationResult calculation =
                calculationService.calculate(
                        credit.getCreditType(),
                        credit.getRequestedAmount(),
                        credit.getInstallmentsQuantity());

        LocalDate today =
                LocalDate.now(clock);

        Instant now =
                clock.instant();

        List<Installment> installments =
                installmentFactory.createInstallments(
                        credit.getId(),
                        1,
                        credit.getInstallmentsQuantity(),
                        calculation.totalAmount(),
                        today,
                        dueDatePolicy);

        credit.contract(
                installments,
                now
        );

        creditRepository.save(credit);

        eventPublisher.publishEvent(
                new CreditContractedEvent(
                        creditId,
                        credit.getCustomerId(),
                        now
                )
        );
    }
}
