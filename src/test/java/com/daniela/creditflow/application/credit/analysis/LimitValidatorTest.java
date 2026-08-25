package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.valueobject.Money;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LimitValidatorTest {

    private LimitValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LimitValidator();
    }

    @Test
    @DisplayName("Should approve credit when amount is within type limit")
    void shouldApproveWhenAmountIsWithinLimit() {

        Credit credit =
                CreditTestFactory.creditWithTypeAndAmount(
                        CreditType.PERSONAL,
                        new Money(new BigDecimal("40000"))
                );

        Customer customer =
                CustomerTestFactory.customer();


        AnalysisResult result =
                validator.handle(
                        credit,
                        customer
                );


        assertThat(result.approved())
                .isTrue();
    }

    @Test
    @DisplayName("Should reject credit when amount exceeds type limit")
    void shouldRejectWhenAmountExceedsLimit() {

        Credit credit =
                CreditTestFactory.creditWithTypeAndAmount(
                        CreditType.PERSONAL,
                        new Money(new BigDecimal("60000"))
                );

        Customer customer =
                CustomerTestFactory.customer();


        AnalysisResult result =
                validator.handle(
                        credit,
                        customer
                );


        assertThat(result.approved())
                .isFalse();

        assertThat(result.reason())
                .isEqualTo(
                        "Credit type limit exceeded"
                );
    }
}