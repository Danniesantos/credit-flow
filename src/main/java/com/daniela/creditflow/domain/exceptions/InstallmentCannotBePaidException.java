package com.daniela.creditflow.domain.exceptions;

public class InstallmentCannotBePaidException extends BusinessRuleException {

    public InstallmentCannotBePaidException() {
        super("Only pending installments can be paid");
    }
}
