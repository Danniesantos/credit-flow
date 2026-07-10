package com.daniela.creditflow.application.installment.policy;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MonthlyDueDatePolicy implements DueDatePolicy {

    @Override
    public LocalDate calculate(int installmentNumber, LocalDate referenceDate) {
        return referenceDate.plusMonths(installmentNumber);
    }
}
