package com.daniela.creditflow.application.installment.policy;

import java.time.LocalDate;

public interface DueDatePolicy {

    LocalDate calculate(int installmentNumber, LocalDate referenceDate);
}
