package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.valueObject.Money;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class IncomeValidatorTest {

    private IncomeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new IncomeValidator();
    }

    @Test
    @DisplayName("Should approve credit when requested amount is within income limit")
    void shouldApproveWhenAmountIsWithinIncomeLimit() {

        Customer customer =
                CustomerTestFactory.customer();

        Credit credit =
                CreditTestFactory.creditWithAmount(
                        new Money(new BigDecimal("50000"))
                );

        AnalysisResult result =
                validator.handle(
                        credit,
                        customer
                );

        assertThat(result.approved())
                .isTrue();
    }

    @Test
    @DisplayName("Should reject credit when requested amount exceeds income limit")
    void shouldRejectWhenAmountExceedsIncomeLimit() {

        Customer customer =
                CustomerTestFactory.customer();

        Credit credit =
                CreditTestFactory.creditWithAmount(
                        new Money(new BigDecimal("70000"))
                );

        AnalysisResult result =
                validator.handle(
                        credit,
                        customer
                );

        assertThat(result.approved())
                .isFalse();

        assertThat(result.reason())
                .isEqualTo(
                        "Requested amount exceeds income limit"
                );
    }
}