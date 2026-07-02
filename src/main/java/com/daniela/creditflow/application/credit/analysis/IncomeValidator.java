package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.customer.model.Customer;
import com.daniela.creditflow.domain.valueObject.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IncomeValidator
        extends CreditAnalysisHandler {

    @Override
    protected AnalysisResult validate(
            Credit credit,
            Customer customer) {

        Money maximumAmount =
                customer.getMonthlyIncome()
                        .multiply(BigDecimal.valueOf(12));

        if (credit.getRequestedAmount()
                .greaterThan(maximumAmount)) {

            return AnalysisResult.failure(
                    "Requested amount exceeds income limit");
        }

        return AnalysisResult.success();
    }
}

