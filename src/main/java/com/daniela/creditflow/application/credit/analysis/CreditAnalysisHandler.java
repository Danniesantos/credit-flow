package com.daniela.creditflow.application.credit.analysis;

import com.daniela.creditflow.application.credit.dto.output.AnalysisResult;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Customer;

public abstract class CreditAnalysisHandler {

    private CreditAnalysisHandler next;

    public CreditAnalysisHandler next(
            CreditAnalysisHandler next) {

        this.next = next;
        return next;
    }

    public AnalysisResult handle(
            Credit credit,
            Customer customer) {

        AnalysisResult result =
                validate(
                        credit,
                        customer);

        if (!result.approved()) {
            return result;
        }

        if (next == null) {
            return AnalysisResult.success();
        }

        return next.handle(
                credit,
                customer);
    }

    protected abstract AnalysisResult validate(
            Credit credit,
            Customer customer);
}
