package com.daniela.creditflow.application.installment.policy;

import java.time.LocalDate;

public class MonthlyDueDatePolicy implements DueDatePolicy {

    @Override
    public LocalDate calculate(int installmentNumber, LocalDate referenceDate) {
        return referenceDate.plusMonths(installmentNumber);
    }
}
