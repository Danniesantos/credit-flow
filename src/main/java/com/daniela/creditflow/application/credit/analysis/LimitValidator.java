package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.valueObject.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LimitValidator
        extends CreditAnalysisHandler {

    @Override
    protected AnalysisResult validate(
            Credit credit,
            Customer customer) {

        Money limit = switch (credit.getCreditType()) {
            case PERSONAL -> new Money(BigDecimal.valueOf(50_000));
            case PAYROLL -> new Money(BigDecimal.valueOf(100_000));
            case BUSINESS -> new Money(BigDecimal.valueOf(500_000));
        };

        if (credit.getRequestedAmount().greaterThan(limit)) {

            return AnalysisResult.failure(
                    "Credit type limit exceeded");
        }

        return AnalysisResult.success();
    }
}

