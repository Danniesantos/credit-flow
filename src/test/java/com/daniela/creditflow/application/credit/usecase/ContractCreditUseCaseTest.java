package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.service.CreditCalculationService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.installment.factory.InstallmentFactory;
import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.repository.CreditRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class ContractCreditUseCaseTest {

    @Mock
    private CreditRepository repository;

    @Mock
    private CreditCalculationService calculationService;

    @Mock
    private CreditService creditService;

    @Mock
    private InstallmentFactory installmentFactory;

    @Mock
    private DueDatePolicy dueDatePolicy;

    @Mock
    private ApplicationEventPublisher eventPublisher;

}