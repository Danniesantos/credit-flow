package com.daniela.creditflow.domain.exceptions;

public class InstallmentAlreadyPaidException extends BusinessRuleException {

    public InstallmentAlreadyPaidException() {
        super("Installment is already paid");
    }
}
