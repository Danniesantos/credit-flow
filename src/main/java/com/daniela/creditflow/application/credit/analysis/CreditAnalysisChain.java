package com.daniela.creditflow.application.credit.analysis;

import org.springframework.stereotype.Component;

@Component
public class CreditAnalysisChain {

    private final ScoreValidator scoreValidator;

    public CreditAnalysisChain(ScoreValidator scoreValidator,
                               IncomeValidator incomeValidator,
                               LimitValidator limitValidator) {
        this.scoreValidator = scoreValidator;


        scoreValidator
                .next(incomeValidator)
                .next(limitValidator);
    }

    public CreditAnalysisHandler chain() {
        return scoreValidator;
    }
}
