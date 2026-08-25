package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.analysis.CreditAnalysisChain;
import com.daniela.creditflow.application.credit.analysis.CreditAnalysisHandler;
import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.application.credit.dto.output.AnalyzeCreditOutput;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.customer.service.CustomerService;
import com.daniela.creditflow.domain.event.CreditApprovedEvent;
import com.daniela.creditflow.domain.event.CreditRejectedEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.CustomerTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyzeCreditUseCaseTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditService creditService;

    @Mock
    private CustomerService customerService;

    @Mock
    private CreditAnalysisChain creditAnalysisChain;

    @Mock
    private CreditAnalysisHandler analysisHandler;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private AnalyzeCreditUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new AnalyzeCreditUseCase(
                creditRepository,
                creditService,
                customerService,
                creditAnalysisChain,
                eventPublisher,
                TestConstants.FIXED_CLOCK
        );
    }

    @Test
    @DisplayName("Should approve credit and publish approved event")
    void shouldApproveCredit() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        Customer customer =
                CustomerTestFactory.customer();

        CreditId id = credit.getId();

        when(creditService.findCredit(id))
                .thenReturn(credit);

        when(customerService.findCustomer(
                credit.getCustomerId()))
                .thenReturn(customer);


        when(creditAnalysisChain.chain())
                .thenReturn(analysisHandler);

        when(analysisHandler.handle(
                credit,
                customer))
                .thenReturn(
                        AnalysisResult.success()
                );


        AnalyzeCreditOutput output =
                useCase.execute(id);


        assertThat(output.status())
                .isEqualTo(
                        CreditStatus.APPROVED
                );


        verify(creditRepository)
                .save(credit);


        verify(eventPublisher)
                .publishEvent(
                        any(CreditApprovedEvent.class)
                );
    }

    @Test
    @DisplayName("Should reject credit and publish rejected event")
    void shouldRejectCredit() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        Customer customer =
                CustomerTestFactory.customer();

        CreditId id =
                credit.getId();


        when(creditService.findCredit(id))
                .thenReturn(credit);

        when(customerService.findCustomer(
                credit.getCustomerId()))
                .thenReturn(customer);


        when(creditAnalysisChain.chain())
                .thenReturn(analysisHandler);


        when(analysisHandler.handle(
                credit,
                customer))
                .thenReturn(
                        AnalysisResult.failure(
                                "Low score"
                        )
                );


        AnalyzeCreditOutput output =
                useCase.execute(id);


        assertThat(output.status())
                .isEqualTo(
                        CreditStatus.REJECTED
                );


        assertThat(output.reason())
                .isEqualTo("Low score");


        verify(creditRepository)
                .save(credit);


        verify(eventPublisher)
                .publishEvent(
                        any(CreditRejectedEvent.class)
                );
    }

    @Test
    @DisplayName("Should load credit and customer before analysis")
    void shouldLoadDependenciesBeforeAnalysis() {

        Credit credit =
                CreditTestFactory.underAnalysisCredit();

        Customer customer =
                CustomerTestFactory.customer();


        when(creditService.findCredit(
                credit.getId()))
                .thenReturn(credit);

        when(customerService.findCustomer(
                credit.getCustomerId()))
                .thenReturn(customer);


        when(creditAnalysisChain.chain())
                .thenReturn(analysisHandler);

        when(analysisHandler.handle(any(), any()))
                .thenReturn(
                        AnalysisResult.success()
                );


        useCase.execute(
                credit.getId()
        );


        verify(creditService)
                .findCredit(
                        credit.getId()
                );

        verify(customerService)
                .findCustomer(
                        credit.getCustomerId()
                );
    }
}

