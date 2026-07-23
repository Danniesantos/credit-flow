package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.calculation.CreditCalculationResult;
import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.installment.factory.InstallmentFactory;
import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.event.CreditContractedEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.model.Installment;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContractCreditUseCase {

    private final CreditRepository creditRepository;
    private final CreditService creditService;
    private final InstallmentFactory installmentFactory;
    private final CreditCalculationService calculationService;
    private final DueDatePolicy dueDatePolicy;
    private final ApplicationEventPublisher eventPublisher;

    public ContractCreditUseCase(CreditRepository creditRepository,
                                 CreditService creditService,
                                 InstallmentFactory installmentFactory,
                                 CreditCalculationService calculationService,
                                 DueDatePolicy dueDatePolicy,
                                 ApplicationEventPublisher eventPublisher) {
        this.creditRepository = creditRepository;
        this.creditService = creditService;
        this.installmentFactory = installmentFactory;
        this.calculationService = calculationService;
        this.dueDatePolicy = dueDatePolicy;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(CreditId creditId) {

        Credit credit = creditService.findCredit(creditId);

        CreditCalculationResult calculation =
                calculationService.calculate(
                        credit.getCreditType(),
                        credit.getRequestedAmount(),
                        credit.getInstallmentsQuantity());

        List<Installment> installments =
                installmentFactory.createInstallments(
                        credit.getId(),
                        credit.getInstallmentsQuantity(),
                        calculation.totalAmount(),
                        LocalDate.now(),
                        dueDatePolicy);

        credit.contract(installments);

        creditRepository.save(credit);

        eventPublisher.publishEvent(
                new CreditContractedEvent(creditId,
                        credit.getCustomerId(),
                        Instant.now()));
    }
}
