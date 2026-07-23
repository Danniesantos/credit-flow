package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class ScoreValidator
        extends CreditAnalysisHandler {

    @Override
    protected AnalysisResult validate(
            Credit credit,
            Customer customer) {

        if (!customer.getCreditScore().isGood()) {
            return AnalysisResult.failure(
                    "Credit score below minimum required");
        }

        return AnalysisResult.success();
    }
}
