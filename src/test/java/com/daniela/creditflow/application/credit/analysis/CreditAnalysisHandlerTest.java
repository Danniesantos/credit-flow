package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreditAnalysisHandlerTest {

    private final Credit credit =
            CreditTestFactory.contractedCredit();

    private final Customer customer =
            CustomerTestFactory.customer();

    private static class TestHandler extends CreditAnalysisHandler {

        private final AnalysisResult result;

        TestHandler(AnalysisResult result) {
            this.result = result;
        }

        @Override
        protected AnalysisResult validate(
                Credit credit,
                Customer customer) {

            return result;
        }
    }

    @Test
    @DisplayName("Should stop chain when validation fails")
    void shouldStopChainWhenValidationFails() {

        AnalysisResult rejected =
                AnalysisResult.failure("Rejected");

        CreditAnalysisHandler handler =
                new TestHandler(rejected);

        CreditAnalysisHandler next =
                mock(CreditAnalysisHandler.class);

        handler.next(next);

        AnalysisResult result =
                handler.handle(
                        credit,
                        customer
                );

        assertThat(result)
                .isEqualTo(rejected);

        verify(next, never())
                .handle(any(), any());
    }

    @Test
    @DisplayName("Should return success when there is no next handler")
    void shouldReturnSuccessWhenThereIsNoNextHandler() {

        CreditAnalysisHandler handler =
                new TestHandler(
                        AnalysisResult.success()
                );

        AnalysisResult result =
                handler.handle(
                        credit,
                        customer
                );

        assertThat(result.approved())
                .isTrue();
    }

    @Test
    @DisplayName("Should delegate to next handler")
    void shouldDelegateToNextHandler() {

        CreditAnalysisHandler handler =
                new TestHandler(
                        AnalysisResult.success()
                );

        CreditAnalysisHandler next =
                mock(CreditAnalysisHandler.class);

        AnalysisResult expected =
                AnalysisResult.success();

        when(next.handle(any(), any()))
                .thenReturn(expected);

        handler.next(next);

        AnalysisResult result =
                handler.handle(credit, customer);

        verify(next)
                .handle(credit, customer);

        assertThat(result)
                .isEqualTo(expected);
    }
}